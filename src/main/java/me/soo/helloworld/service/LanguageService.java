package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.enumeration.LanguageLevel;
import me.soo.helloworld.enumeration.LanguageStatus;
import me.soo.helloworld.exception.language.DuplicateLanguageException;
import me.soo.helloworld.exception.language.InvalidLanguageLevelException;
import me.soo.helloworld.exception.language.LanguageLimitExceededException;
import me.soo.helloworld.mapper.LanguageMapper;
import me.soo.helloworld.model.language.LanguageData;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static me.soo.helloworld.util.CacheNames.REDIS_CACHE_MANAGER;
import static me.soo.helloworld.util.CacheNames.USER_PROFILE;

@Service
@RequiredArgsConstructor
public class LanguageService {
    private static final int MAX_TOTAL_LANGUAGES = 16;

    private final LanguageMapper languageMapper;

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

    @CacheEvict(key = "#userId", value = USER_PROFILE, cacheManager = REDIS_CACHE_MANAGER)
    public void addLanguages(String userId, List<LanguageData> newLanguages, LanguageStatus status) {
        int dbLangCounts = languageMapper.countLanguages(userId, status);

        if (dbLangCounts + newLanguages.size() > status.getAddLimit()) {
            throw new LanguageLimitExceededException("해당 status 로 추가 가능한 언어의 개수를 초과하였습니다.");
        }

        validateLevel(newLanguages, status);

        List<LanguageData> existingLanguages = getLanguages(userId);
        checkDuplicateLanguage(existingLanguages, newLanguages);

        languageMapper.insertLanguages(userId, newLanguages, status);
    }

    @Transactional(readOnly = true)
    public List<LanguageData> getLanguages(String userId) {
        return languageMapper.getLanguages(userId);
    }

    @CacheEvict(key = "#userId", value = USER_PROFILE, cacheManager = REDIS_CACHE_MANAGER)
    public void modifyLanguageLevels(String userId, List<LanguageData> languageNewLevels, LanguageStatus status) {
        if (status.equals(LanguageStatus.NATIVE)) {
            throw new InvalidLanguageLevelException("언어 status 가 모국어(NATIVE)로 등록되어 있는 언어들은 레벨을 변경할 수 없습니다.");
        }

        validateLevel(languageNewLevels, status);
        languageMapper.updateLevels(userId, languageNewLevels, status);
    }

    @CacheEvict(key = "#userId", value = USER_PROFILE, cacheManager = REDIS_CACHE_MANAGER)
    public void deleteLanguages(String userId, List<Integer> languages) {
        if (languages.size() > MAX_TOTAL_LANGUAGES) {
            throw new LanguageLimitExceededException("삭제하려는 언어의 개수는 등록 가능한 언어의 최대 개수를 넘을 수 없습니다.");
        }

        languageMapper.deleteLanguages(userId, languages);
    }

    /*
        중복된 언어 추가 요청이 들어왔는지 확인하는 메소드

        1. 새로 요청받은 언어 목록 중에 중복되는 언어가 있는 경우 예외 발생
        2. DB 내에 추가된 언어 목록과 비교해서 중복 요청이 들어온 경우 예외 발생
     */
    private void checkDuplicateLanguage(List<LanguageData> existingLanguages, List<LanguageData> newLanguages) {
        List<Integer> newLangIds = extractLanguageIdsOnly(newLanguages);

        // 새로 요청 받은 언어목록을 확인
        if (newLangIds.size() != newLangIds.stream().distinct().count()) {
            throw new DuplicateLanguageException("새로 요청 받은 언어들 중에 중복되는 언어가 존재합니다. 중복 선택은 불가능 합니다.");
        }

        List<Integer> existingLangIds = extractLanguageIdsOnly(existingLanguages);

        // 기존 언어목록과 새로 요청받은 언어목록을 비교
        if (newLangIds.stream().anyMatch(existingLangIds::contains)) {
            throw new DuplicateLanguageException("이미 추가되어 있는 언어는 다시 추가할 수 없습니다.");
        }
    }

    private List<Integer> extractLanguageIdsOnly(List<LanguageData> languages) {
        return languages.stream()
                .map(langId -> languages.get(languages.indexOf(langId)).getId())
                .collect(Collectors.toList());
    }


    /*
        추가 할 Status 맞게 언어 레벨을 설정했는지 확인하는 메소드

        1. Status 가 Native 인 경우
        - 추가하는 언어 모두 레벨이 NATIVE 로 이루어져 있어야 합니다.
        2. Status 가 Native 가 아닌 경우 (CAN_SPEAK or LEARNING)
        - 어떤 언어도 레벨이 NATIVE 가 되어서는 안됩니다.
     */
    private void validateLevel(List<LanguageData> languages, LanguageStatus status) {
        boolean isLevelValid;

        switch (status) {
            case NATIVE:
                isLevelValid = languages.stream()
                                            .allMatch(level -> languages.get(languages.indexOf(level))
                                            .getLevel().equals(LanguageLevel.NATIVE));
                break;
            case CAN_SPEAK:
            case LEARNING:
                isLevelValid = languages.stream()
                                        .noneMatch(level -> languages.get(languages.indexOf(level))
                                        .getLevel().equals(LanguageLevel.NATIVE));
                break;
            default:
                throw new InvalidLanguageLevelException("해당 언어 status 는 존재하지 않습니다. 모국어(NATIVE), 구사 가능언어(CAN_SPEAK)," +
                        " 학습 중인 언어(LEARNING) 중 한 가지만 선택해주세요.");
        }

        if (!isLevelValid) {
            throw new InvalidLanguageLevelException("추가하실 언어에 대한 레벨을 잘못 입력하셨습니다. 모국어의 언어 레벨은 NATIVE 레벨로만 설정이 가능하며," +
                    " 모국어가 아닌 언어에는 NATIVE 레벨로 설정이 불가능합니다.");
        }
    }


}
