package me.soo.helloworld.util.validator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.soo.helloworld.enumeration.LanguageLevel;
import me.soo.helloworld.enumeration.LanguageStatus;
import me.soo.helloworld.exception.language.InvalidLanguageLevelException;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LanguageLevelValidator {

    /*
        1) 언어 추가 시 Status 에 맞게 언어 레벨을 설정했는지 확인하는 경우

        * Status 가 Native 인 경우
        - 추가하는 언어 모두 레벨이 NATIVE 로 이루어져 있어야 합니다.
        * Status 가 Native 가 아닌 경우 (CAN_SPEAK or LEARNING)
        - 어떤 언어도 레벨이 NATIVE 가 되어서는 안됩니다.

        2) 프로필 조회 시 Status 에 맞게 언어 레벨을 설정했는지 확인하는 경우

        * 프로필 조회 시 검색 조건으로 언어 레벨을 설정하는 부분은 Learning Language 에 대해서만 이루어집니다.
        - 따라서 학습 중인 언어에 대한 언어 레벨 검색 시 레벨 조건에 Native 가 들어갈 수 없습니다.
        - 이외에는 모든 레벨을 자유롭게 선택 가능합니다.
 */
    public static void validateLevel(Collection<LanguageLevel> levels, LanguageStatus status) {
        boolean isLevelValid;

        switch (status) {
            case NATIVE:
                isLevelValid = levels.stream().allMatch(level -> level.equals(LanguageLevel.NATIVE));
                break;
            case CAN_SPEAK:
            case LEARNING:
                isLevelValid = levels.stream().noneMatch(level -> level.equals(LanguageLevel.NATIVE));
                break;
            default:
                throw new InvalidLanguageLevelException("해당 언어 status 는 존재하지 않습니다. 모국어(NATIVE), 구사 가능언어(CAN_SPEAK)," +
                        " 학습 중인 언어(LEARNING) 중 한 가지만 선택해주세요.");
        }

        if (!isLevelValid) {
            throw new InvalidLanguageLevelException("언어에 대한 레벨을 잘못 입력하셨습니다. 모국어의 언어 레벨은 NATIVE 레벨로만 설정이 가능하며," +
                    " 모국어가 아닌 언어는 NATIVE 레벨로 설정이 불가능합니다.");
        }
    }
}
