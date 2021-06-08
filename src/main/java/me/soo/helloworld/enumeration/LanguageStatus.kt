package me.soo.helloworld.enumeration

import me.soo.helloworld.exception.language.InvalidLanguageLevelException
import me.soo.helloworld.util.handler.EnumCategoryTypeHandler
import org.apache.ibatis.type.MappedTypes

enum class LanguageStatus(

    private val category: Int,

    val addLimit: Int,

    private val expectedLevel: Set<LanguageLevel>
) : EnumCategory {

    LEARNING(
        1, 8,
        setOf(
            LanguageLevel.BEGINNER,
            LanguageLevel.ELEMENTARY,
            LanguageLevel.INTERMEDIATE,
            LanguageLevel.UPPER_INTERMEDIATE,
            LanguageLevel.ADVANCED,
            LanguageLevel.PROFICIENCY
        )
    ),

    CAN_SPEAK(2, 4, setOf(LanguageLevel.PROFICIENCY)),

    NATIVE(3, 4, setOf(LanguageLevel.NATIVE));

    override fun getCategory() = category

    /*
        1) 언어 추가 시 Status 에 맞게 언어 레벨을 설정했는지 확인하는 경우

        * Status 가 Native 인 경우
            - 추가하는 언어 모두 레벨이 NATIVE 로 이루어져 있어야 합니다.
        * Status 가 Native 가 아닌 경우 (CAN_SPEAK or LEARNING)
            - CAN_SPEAK 에 해당하는 언어는 'PROFICIENCY' 레벨만 가질 수 있도록 변경되었습니다.
            - LEARNING 에 해당하는 언어는 NATIVE 를 제외한 모든 레벨을 가질 수 있습니다.

        2) 프로필 조회 시 Status 에 맞게 언어 레벨을 설정했는지 확인하는 경우

        * 프로필 조회 시 검색 조건으로 언어 레벨을 설정하는 부분은 상대방의 학습 언어에 대해서만 가능합니다.
            - 따라서 검색 조건으로 언어 레벨을 지정할 때 레벨 조건에 NATIVE 가 들어갈 수 없습니다.
    */
    fun validateLevel(levels: Collection<LanguageLevel>) =
        if (levels.any { !this.expectedLevel.contains(it) }) {
            throw InvalidLanguageLevelException("등록할 언어 레벨을 다시 확인해주세요. 현재 status '$this' 로 등록할 수 있는 언어 레벨은 오직 '${this.expectedLevel}' 입니다.")
        } else Unit

    @MappedTypes(LanguageStatus::class)
    class TypeHandler : EnumCategoryTypeHandler<LanguageStatus>(LanguageStatus::class.java)
}
