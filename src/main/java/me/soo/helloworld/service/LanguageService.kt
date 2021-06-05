package me.soo.helloworld.service

import me.soo.helloworld.enumeration.LanguageStatus
import me.soo.helloworld.exception.language.DuplicateLanguageException
import me.soo.helloworld.exception.language.InvalidLanguageLevelException
import me.soo.helloworld.exception.language.LanguageLimitExceededException
import me.soo.helloworld.mapper.LanguageMapper
import me.soo.helloworld.model.language.LanguageData
import me.soo.helloworld.model.language.LanguageRequest
import me.soo.helloworld.util.constant.CacheNames.REDIS_CACHE_MANAGER
import me.soo.helloworld.util.constant.CacheNames.USER_PROFILE
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service

@Service
class LanguageService(private val languageMapper: LanguageMapper) {

    /*
        해당 사용자의 아이디를 이용해 언어 정보를 추가하는 메소드

        언어 status 에 따른 추가 개수 제한을 두었습니다.
        - 학습중인 언어(LEARNING): 8개
        - 구사가능 언어(CAN_SPEAK): 4개
        - 모국어(NATIVE): 4개

        예상 예외 시나리오
        - 추가 가능한 최대 언어의 개수가 초과했을 경우
        - 중복된 언어가 추가된 경우
            1) 새롭게 요청 받은 언어 목록 내에서 중복 언어가 존재하는 경우
            2) 기존 DB 에 저장된 전체 언어 목록과 비교했을 때 중복 요청이 들어온 경우
        - 언어 레벨이 맞지 않는 경우
            1) 추가할 언어의 status 는 NATIVE 인데, level 이 NATIVE 로 지정되어 있지 않은 경우
            2) 추가할 언어의 status 는 NATIVE 가 아닌데, level 이 NATIVE 로 지정되어 있는 경우
     */
    @CacheEvict(key = "#userId", value = [USER_PROFILE], cacheManager = REDIS_CACHE_MANAGER)
    fun addLanguages(userId: String, newLanguages: List<LanguageRequest>, status: LanguageStatus) {
        val currLanguages = languageMapper.getLanguages(userId)

        verifyBelowStatusAddLimit(currLanguages, newLanguages, status)
        status.validateLevel(newLanguages.map { it.level })
        verifyNoDuplicate(currLanguages.map { it.id }, newLanguages.map { it.id })

        languageMapper.insertLanguages(userId, newLanguages, status)
    }

    @CacheEvict(key = "#userId", value = [USER_PROFILE], cacheManager = REDIS_CACHE_MANAGER)
    fun modifyLevels(userId: String, newLevels: List<LanguageRequest>, status: LanguageStatus) {
        if (status == LanguageStatus.CAN_SPEAK || status == LanguageStatus.NATIVE) {
            throw InvalidLanguageLevelException("언어 status 가 ${status}로 등록되어 있는 언어들은 레벨을 변경할 수 없습니다.")
        }

        status.validateLevel(newLevels.map { it.level })
        languageMapper.updateLevels(userId, newLevels, status)
    }

    @CacheEvict(key = "#userId", value = [USER_PROFILE], cacheManager = REDIS_CACHE_MANAGER)
    fun deleteLanguages(userId: String, langIds: List<Int>) = languageMapper.deleteLanguages(userId, langIds)

    private fun verifyBelowStatusAddLimit(
        currLanguages: List<LanguageData>,
        newLanguages: List<LanguageRequest>,
        status: LanguageStatus
    ) {
        val currLangCounts = currLanguages.count { it.status == status }

        if (currLangCounts + newLanguages.size > status.addLimit) {
            throw LanguageLimitExceededException("해당 status 로 추가 가능한 언어의 개수를 초과하였습니다.")
        }
    }

    private fun verifyNoDuplicate(currLangIds: List<Int>, newLangIds: List<Int>) {
        if (newLangIds.size != newLangIds.distinct().size) {
            throw DuplicateLanguageException("새로 요청 받은 언어목록 내부에 중복되는 언어가 존재합니다. 중복 선택은 불가능 합니다.")
        }

        if (newLangIds.any { currLangIds.contains(it) }) {
            throw DuplicateLanguageException("이미 추가되어 있는 언어는 다시 추가할 수 없습니다.")
        }
    }
}
