package me.soo.helloworld.service.profile;

import me.soo.helloworld.enumeration.LanguageLevel;
import me.soo.helloworld.model.condition.SearchConditionsRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static me.soo.helloworld.TestLanguages.ENGLISH;
import static me.soo.helloworld.TestLanguages.KOREAN;
import static me.soo.helloworld.util.validator.AgeRangeValidator.MAX_AGE_RANGE;
import static me.soo.helloworld.util.validator.AgeRangeValidator.MIN_AGE_RANGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class SearchProfilesValidatorTest {

    private final String noSpeakLangExceptionMessage = "사용자 검색 시 상대방의 구사언어 정보는 필수로 입력하셔야 합니다.";

    private final String noLearningLangExceptionMessage = "사용자 검색 시 상대방이 학습하고 있는 언어 정보는 필수로 입력하셔야 합니다.";

    private final String noLearningLangLevelsExceptionMessage = "사용자 검색 시 상대방이 학습하고 있는 언어에 대한 레벨정보는 필수로 입력하셔야 합니다.";

    private final String inappropriateGenderExceptionMessage = "성별을 검색 조건으로 지정하기 위해서는 'M'(Male), 'F'(Female), 'O'(Other)의 세 조건 중 하나만 입력이 가능합니다.";

    private final String inappropriateAgeRangeExceptionMessage = "나이를 검색조건으로 지정할 때, 최대나이는 100살 이상, 최소나이는 9살 이하로 내려갈 수 없으며, " +
            "최소 나이는 항상 최대 나이보다 작은 값을 유지해야 합니다.";

    private final int belowMinAgeLimit = MIN_AGE_RANGE - 1;

    private final int aboveMaxAgeLimit = MAX_AGE_RANGE + 1;

    Validator validator;

    List<LanguageLevel> levels;

    /*
        1. validator 필수 입력조건 테스트
        - 검색을 위해 반드시 입력이 필요한 조건인 @NotNull 데이터 들이 존재하지 않을 때와 존재할 때 validator 가 제대로 검증하는지 테스트
     */
    SearchConditionsRequest notNullRequestWithoutSpeakLanguage;

    SearchConditionsRequest notNullRequestWithoutLearningLanguage;

    SearchConditionsRequest notNullRequestWithoutLearningLanguageLevels;

    SearchConditionsRequest notNullRequestWithoutSpeakAndLearningLanguage;

    SearchConditionsRequest notNullRequestWithoutSpeakLanguageAndLearningLanguageLevels;

    SearchConditionsRequest notNullRequestWithoutLearningLanguageAndLearningLanguageLevels;

    SearchConditionsRequest notNullRequestWithNoMandatoryConditionsSelected;

    SearchConditionsRequest notNullRequestWithAllRequiredDataSelected;

    /*
        2. validator 선택 입력조건 테스트
            * 성별은 'M'(Male), 'F'(Female), 'O'(Other) 만 조건으로 입력이 가능합니다. 잘못된 글자가 들어오는 경우 예외가 발생합니다.
            * 나이는 최소값(minAge)과 최대값(maxAge)을 지정해 범위를 선택할 수 있습니다.
                1) 최소값이 지정되지 않은 경우 기본 최소값은 10입니다.
                2) 최대값이 지정되지 않은 경우 기본 최대값은 99입니다.
                3) 최소값은 항상 최대값보다 같거나 작아야합니다.
     */
    SearchConditionsRequest genderRequestOtherThanOneOfMFO;

    SearchConditionsRequest genderRequestWithM;

    SearchConditionsRequest genderRequestWithF;

    SearchConditionsRequest genderRequestWithO;

    SearchConditionsRequest onlyMaxAgeRequestOverMaxAgeLimit;

    SearchConditionsRequest onlyMaxAgeRequestWithinMaxAgeLimit;

    SearchConditionsRequest onlyMaxAgeRequestBelowMinAgeLimit;

    SearchConditionsRequest onlyMinAgeRequestOverMaxAgeLimit;

    SearchConditionsRequest onlyMinAgeRequestWithinMixAgeLimit;

    SearchConditionsRequest onlyMixAgeRequestBelowMinAgeLimit;

    SearchConditionsRequest bothAgesRequestWithMinBelowMinAgeLimit;

    SearchConditionsRequest bothAgesRequestWithMaxOverMaxAgeLimit;

    SearchConditionsRequest bothAgesRequestWithMaxOverMaxAndMinBelowMinAgeLimit;

    SearchConditionsRequest bothAgesRequestWithinLimitButMinGreaterThanMax;

    SearchConditionsRequest bothAgesRequestWithinLimitAndMaxEqualOrGreaterThanMin;

    /*
        모든 조건이 유효하거나 유효하지 않을 때 테스트
     */
    SearchConditionsRequest noneValidConditions;

    SearchConditionsRequest allValidConditions;

    @BeforeEach
    public void initValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    public void insertLanguageLevels() {
        levels = new ArrayList<>();
        levels.add(LanguageLevel.BEGINNER);
        levels.add(LanguageLevel.ELEMENTARY);
        levels.add(LanguageLevel.INTERMEDIATE);
        levels.add(LanguageLevel.UPPER_INTERMEDIATE);
    }

    @Test
    @DisplayName("조건을 사용해 다른 사용자를 검색할 때 구사 가능 언어를 조건으로 선택하지 않으면 validation 위반이 발생합니다.")
    public void searchUserProfilesFailWithoutSpeakLanguageSelected() {
        notNullRequestWithoutSpeakLanguage = SearchConditionsRequest.builder()
                .learningLanguage(ENGLISH)
                .learningLanguageLevel(levels)
                .build();

        Set<ConstraintViolation<SearchConditionsRequest>> violations = validator.validate(notNullRequestWithoutSpeakLanguage);

        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsOnly(noSpeakLangExceptionMessage);
    }

    @Test
    @DisplayName("조건을 사용해 다른 사용자를 검색할 때 학습 중인 언어를 조건으로 선택하지 않으면 validation 위반이 발생합니다.")
    public void searchUserProfilesFailWithoutLearningLanguageSelected() {
        notNullRequestWithoutLearningLanguage = SearchConditionsRequest.builder()
                .speakLanguage(KOREAN)
                .learningLanguageLevel(levels)
                .build();

        Set<ConstraintViolation<SearchConditionsRequest>> violations = validator.validate(notNullRequestWithoutLearningLanguage);

        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsOnly(noLearningLangExceptionMessage);
    }

    @Test
    @DisplayName("조건을 사용해 다른 사용자를 검색할 때 학습 중인 언어의 레벨에 대한 검색 범위를 선택하지 않으면 validation 위반이 발생합니다.")
    public void searchUserProfilesFailWithoutLearningLanguageLevelsSelected() {
        notNullRequestWithoutLearningLanguageLevels = SearchConditionsRequest.builder()
                .speakLanguage(KOREAN)
                .learningLanguage(ENGLISH)
                .build();

        Set<ConstraintViolation<SearchConditionsRequest>> violations = validator.validate(notNullRequestWithoutLearningLanguageLevels);

        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsOnly(noLearningLangLevelsExceptionMessage);
    }

    @Test
    @DisplayName("조건을 사용해 다른 사용자를 검색할 때 구사 가능 언어와 학습 중인 언어를 조건으로 선택하지 않으면 validation 위반이 발생합니다.")
    public void searchUserProfilesFailWithoutBothSpeakAndLearningLanguageSelected() {
        notNullRequestWithoutSpeakAndLearningLanguage = SearchConditionsRequest.builder()
                .learningLanguageLevel(levels)
                .build();

        Set<ConstraintViolation<SearchConditionsRequest>> violations = validator.validate(notNullRequestWithoutSpeakAndLearningLanguage);

        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 2);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(noSpeakLangExceptionMessage, noLearningLangExceptionMessage);
    }

    @Test
    @DisplayName("조건을 사용해 다른 사용자를 검색할 때 구사 가능 언어와 학습 중인 언어의 레벨 범위를 조건으로 선택하지 않으면 validation 위반이 발생합니다.")
    public void searchUserProfilesFailWithoutSpeakLanguageAndLearningLanguageLevelsSelected() {
        notNullRequestWithoutSpeakLanguageAndLearningLanguageLevels = SearchConditionsRequest.builder()
                .learningLanguage(ENGLISH)
                .build();

        Set<ConstraintViolation<SearchConditionsRequest>> violations = validator.validate(notNullRequestWithoutSpeakLanguageAndLearningLanguageLevels);

        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 2);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(noSpeakLangExceptionMessage, noLearningLangLevelsExceptionMessage);
    }

    @Test
    @DisplayName("조건을 사용해 다른 사용자를 검색할 때 학습 중인 언어와 해당 언어에 대한 레벨의 검색 범위를 조건으로 선택하지 않으면 validation 위반이 발생합니다.")
    public void searchUserProfilesFailWithoutLearningLanguageAndLearningLanguageLevelsSelected() {
        notNullRequestWithoutLearningLanguageAndLearningLanguageLevels = SearchConditionsRequest.builder()
                .speakLanguage(KOREAN)
                .build();

        Set<ConstraintViolation<SearchConditionsRequest>> violations = validator.validate(notNullRequestWithoutLearningLanguageAndLearningLanguageLevels);

        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 2);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(noLearningLangExceptionMessage, noLearningLangLevelsExceptionMessage);
    }

    @Test
    @DisplayName("조건을 사용해 다른 사용자를 검색할 때 필수 선택 조건이 하나도 들어가 있지 않는 경우에도 validation 위반이 발생합니다.")
    public void searchUserProfilesFailWithoutAnyObligatoryConditionsSelected() {
        notNullRequestWithNoMandatoryConditionsSelected = SearchConditionsRequest.builder()
                .gender("M")
                .minAge(MIN_AGE_RANGE)
                .maxAge(MAX_AGE_RANGE)
                .build();

        Set<ConstraintViolation<SearchConditionsRequest>> violations = validator.validate(notNullRequestWithNoMandatoryConditionsSelected);

        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 3);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(noSpeakLangExceptionMessage, noLearningLangExceptionMessage, noLearningLangLevelsExceptionMessage);
    }

    @Test
    @DisplayName("조건을 사용해 다른 사용자를 검색할 때 필수 정보가 모두 선택되어 있는 경우 validation 검증에 통과합니다.")
    public void searchUserProfilesSuccessWithAllNecessaryConditionsSelected() {
        notNullRequestWithAllRequiredDataSelected = SearchConditionsRequest.builder()
                .speakLanguage(KOREAN)
                .learningLanguage(ENGLISH)
                .learningLanguageLevel(levels)
                .build();

        Set<ConstraintViolation<SearchConditionsRequest>> violations = validator.validate(notNullRequestWithAllRequiredDataSelected);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("성별을 선택조건으로 지정해 다른 사용자를 검색할 때 M(Male), F(Female), O(Other) 중 한 글자가 아닌 다른 글자/단어를 사용해 요청을 보낼 시 validation 위반이 발생합니다.")
    public void searchUserProfilesFailWithGenderOtherThanOneOfMFO() {
        genderRequestOtherThanOneOfMFO = SearchConditionsRequest.builder()
                .gender("MF")
                .speakLanguage(KOREAN)
                .learningLanguage(ENGLISH)
                .learningLanguageLevel(levels)
                .build();

        Set<ConstraintViolation<SearchConditionsRequest>> violations = validator.validate(genderRequestOtherThanOneOfMFO);

        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsOnly(inappropriateGenderExceptionMessage);
    }

    @Test
    @DisplayName("성별을 선택조건으로 지정해 다른 사용자를 검색할 때 M(Male), F(Female), O(Other) 중 한 글자인 M을 선택해 요청을 보낼 시 validation 검증에 통과합니다.")
    public void searchUserProfilesSuccessWithGenderM() {
        genderRequestWithM = SearchConditionsRequest.builder()
                .gender("M")
                .speakLanguage(KOREAN)
                .learningLanguage(ENGLISH)
                .learningLanguageLevel(levels)
                .build();

        Set<ConstraintViolation<SearchConditionsRequest>> violations = validator.validate(genderRequestWithM);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("성별을 선택조건으로 지정해 다른 사용자를 검색할 때 M(Male), F(Female), O(Other) 중 한 글자인 F를 선택해 요청을 보낼 시 validation 검증에 통과합니다.")
    public void searchUserProfilesSuccessWithGenderF() {
        genderRequestWithF = SearchConditionsRequest.builder()
                .gender("F")
                .speakLanguage(KOREAN)
                .learningLanguage(ENGLISH)
                .learningLanguageLevel(levels)
                .build();

        Set<ConstraintViolation<SearchConditionsRequest>> violations = validator.validate(genderRequestWithF);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("성별을 선택조건으로 지정해 다른 사용자를 검색할 때 M(Male), F(Female), O(Other) 중 한 글자인 O를 선택해 요청을 보낼 시 validation 검증에 통과합니다.")
    public void searchUserProfilesSuccessWithGenderO() {
        genderRequestWithO = SearchConditionsRequest.builder()
                .gender("O")
                .speakLanguage(KOREAN)
                .learningLanguage(ENGLISH)
                .learningLanguageLevel(levels)
                .build();

        Set<ConstraintViolation<SearchConditionsRequest>> violations = validator.validate(genderRequestWithO);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("최대 나이만을 선택조건으로 지정해 다른 사용자를 검색할 때 설정된 최대 나이 조건이 최대 나이 제한보다 클 시 validation 위반이 발생합니다.")
    public void searchUnitProfilesFailWithMaxAgeAloneOverMaxAgeLimit() {
        onlyMaxAgeRequestOverMaxAgeLimit = SearchConditionsRequest.builder()
                .gender("M")
                .maxAge(aboveMaxAgeLimit)
                .speakLanguage(KOREAN)
                .learningLanguage(ENGLISH)
                .learningLanguageLevel(levels)
                .build();

        Set<ConstraintViolation<SearchConditionsRequest>> violations = validator.validate(onlyMaxAgeRequestOverMaxAgeLimit);

        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsOnly(inappropriateAgeRangeExceptionMessage);
    }

    @Test
    @DisplayName("최대 나이만을 선택조건으로 지정해 다른 사용자를 검색할 때 설정된 최대 나이 조건이 최소 나이 제한의 범위보다 작을 시 validation 위반이 발생합니다.")
    public void searchUnitProfilesFailWithMaxAgeAloneBelowMinAgeLimit() {
        onlyMaxAgeRequestBelowMinAgeLimit = SearchConditionsRequest.builder()
                .gender("M")
                .maxAge(belowMinAgeLimit)
                .speakLanguage(KOREAN)
                .learningLanguage(ENGLISH)
                .learningLanguageLevel(levels)
                .build();

        Set<ConstraintViolation<SearchConditionsRequest>> violations = validator.validate(onlyMaxAgeRequestBelowMinAgeLimit);

        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsOnly(inappropriateAgeRangeExceptionMessage);
    }

    @Test
    @DisplayName("최대 나이만을 선택조건으로 지정해 다른 사용자를 검색할 때 설정된 최대 나이 조건이 딱 최대 나이 제한의 경계일 경우 validation 검증을 통과합니다.")
    public void searchUnitProfilesSuccessWithMaxAgeAloneAtExactMaxAgeLimit() {
        onlyMaxAgeRequestWithinMaxAgeLimit = SearchConditionsRequest.builder()
                .gender("M")
                .maxAge(MAX_AGE_RANGE)
                .speakLanguage(KOREAN)
                .learningLanguage(ENGLISH)
                .learningLanguageLevel(levels)
                .build();

        Set<ConstraintViolation<SearchConditionsRequest>> violations = validator.validate(onlyMaxAgeRequestWithinMaxAgeLimit);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("최대 나이만을 선택조건으로 지정해 다른 사용자를 검색할 때 설정된 최대 나이 조건이 딱 최소 나이 제한의 경계일 경우에도 validation 검증을 통과합니다.")
    public void searchUnitProfilesSuccessWithMaxAgeAloneAtExactMinAgeLimit() {
        onlyMaxAgeRequestWithinMaxAgeLimit = SearchConditionsRequest.builder()
                .gender("M")
                .maxAge(MIN_AGE_RANGE)
                .speakLanguage(KOREAN)
                .learningLanguage(ENGLISH)
                .learningLanguageLevel(levels)
                .build();

        Set<ConstraintViolation<SearchConditionsRequest>> violations = validator.validate(onlyMaxAgeRequestWithinMaxAgeLimit);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("최소 나이만을 선택조건으로 지정해 다른 사용자를 검색할 때 설정된 최소 나이 조건이 최대 나이 제한 보다 클 경우 validation 위반이 발생합니다.")
    public void searchUnitProfilesFailWithMinAgeAloneOverMaxAgeLimit() {
        onlyMinAgeRequestOverMaxAgeLimit = SearchConditionsRequest.builder()
                .gender("M")
                .minAge(aboveMaxAgeLimit)
                .speakLanguage(KOREAN)
                .learningLanguage(ENGLISH)
                .learningLanguageLevel(levels)
                .build();

        Set<ConstraintViolation<SearchConditionsRequest>> violations = validator.validate(onlyMinAgeRequestOverMaxAgeLimit);

        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsOnly(inappropriateAgeRangeExceptionMessage);
    }

    @Test
    @DisplayName("최소 나이만을 선택조건으로 지정해 다른 사용자를 검색할 때 설정된 최소 나이 조건이 최소 나이 제한 보다 작을 경우 validation 위반이 발생합니다.")
    public void searchUnitProfilesFailWithMixAgeAloneBelowMinAgeLimit() {
        onlyMixAgeRequestBelowMinAgeLimit = SearchConditionsRequest.builder()
                .gender("M")
                .maxAge(belowMinAgeLimit)
                .speakLanguage(KOREAN)
                .learningLanguage(ENGLISH)
                .learningLanguageLevel(levels)
                .build();

        Set<ConstraintViolation<SearchConditionsRequest>> violations = validator.validate(onlyMixAgeRequestBelowMinAgeLimit);

        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsOnly(inappropriateAgeRangeExceptionMessage);
    }

    @Test
    @DisplayName("최소 나이만을 선택조건으로 지정해 다른 사용자를 검색할 때 설정된 최소 나이 조건이 기본 최대 나이 제한의 경계에 설정될 경우에도 validation 검증을 통과합니다.")
    public void searchUnitProfilesSuccessWithMinAgeAloneAtExactMaxAgeLimit() {
        onlyMinAgeRequestWithinMixAgeLimit = SearchConditionsRequest.builder()
                .gender("M")
                .maxAge(MAX_AGE_RANGE)
                .speakLanguage(KOREAN)
                .learningLanguage(ENGLISH)
                .learningLanguageLevel(levels)
                .build();

        Set<ConstraintViolation<SearchConditionsRequest>> violations = validator.validate(onlyMinAgeRequestWithinMixAgeLimit);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("최소 나이만을 선택조건으로 지정해 다른 사용자를 검색할 때 설정된 최소 나이 조건이 기본 최소 나이 제한의 경계에 설정될 경우에도 validation 검증을 통과합니다.")
    public void searchUnitProfilesSuccessWithMinAgeAloneAtExactMinAgeLimit() {
        onlyMinAgeRequestWithinMixAgeLimit = SearchConditionsRequest.builder()
                .gender("M")
                .maxAge(MIN_AGE_RANGE)
                .speakLanguage(KOREAN)
                .learningLanguage(ENGLISH)
                .learningLanguageLevel(levels)
                .build();

        Set<ConstraintViolation<SearchConditionsRequest>> violations = validator.validate(onlyMinAgeRequestWithinMixAgeLimit);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("최대, 최소 나이를 모두 선택조건으로 지정해 다른 사용자를 검색할 때 설정된 최소 나이 조건이 기본 최소 나이 제한보다 작을 경우 validation 위반이 발생합니다.")
    public void searchUnitProfilesFailWithBothMinAndMaxAgesWithMinBelowMinAgeLimit() {
        bothAgesRequestWithMinBelowMinAgeLimit = SearchConditionsRequest.builder()
                .gender("M")
                .minAge(belowMinAgeLimit)
                .maxAge(MAX_AGE_RANGE)
                .speakLanguage(KOREAN)
                .learningLanguage(ENGLISH)
                .learningLanguageLevel(levels)
                .build();

        Set<ConstraintViolation<SearchConditionsRequest>> violations = validator.validate(bothAgesRequestWithMinBelowMinAgeLimit);

        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsOnly(inappropriateAgeRangeExceptionMessage);
    }

    @Test
    @DisplayName("최대, 최소 나이를 모두 선택조건으로 지정해 다른 사용자를 검색할 때 설정된 최대 나이 조건이 기본 최대 나이 제한보다 클 경우 validation 위반이 발생합니다.")
    public void searchUnitProfilesFailWithBothMinAndMaxAgesWithMaxOverMaxAgeLimit() {
        bothAgesRequestWithMaxOverMaxAgeLimit = SearchConditionsRequest.builder()
                .gender("M")
                .minAge(MIN_AGE_RANGE)
                .maxAge(aboveMaxAgeLimit)
                .speakLanguage(KOREAN)
                .learningLanguage(ENGLISH)
                .learningLanguageLevel(levels)
                .build();

        Set<ConstraintViolation<SearchConditionsRequest>> violations = validator.validate(bothAgesRequestWithMaxOverMaxAgeLimit);

        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsOnly(inappropriateAgeRangeExceptionMessage);
    }

    @Test
    @DisplayName("최대, 최소 나이를 모두 선택조건으로 지정해 다른 사용자를 검색할 때 설정된 최소 나이 조건이 기본 최소 나이 제한보다 작고, " +
            "최대 나이 조건이 기본 최대 나이 제한보다 클 경우 validation 위반이 발생합니다.")
    public void searchUnitProfilesFailWithBothMinAndMaxAgesWithMaxOverMaxAndMinBelowMinAgeLimit() {
        bothAgesRequestWithMaxOverMaxAndMinBelowMinAgeLimit = SearchConditionsRequest.builder()
                .gender("M")
                .minAge(belowMinAgeLimit)
                .maxAge(aboveMaxAgeLimit)
                .speakLanguage(KOREAN)
                .learningLanguage(ENGLISH)
                .learningLanguageLevel(levels)
                .build();

        Set<ConstraintViolation<SearchConditionsRequest>> violations = validator.validate(bothAgesRequestWithMaxOverMaxAndMinBelowMinAgeLimit);

        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsOnly(inappropriateAgeRangeExceptionMessage);
    }

    @Test
    @DisplayName("최대, 최소 나이를 모두 선택조건으로 지정해 다른 사용자를 검색할 때 설정된 최소, 최대 나이 조건이 모두 제한조건 범위 내에 존재하지만 " +
            "최소 값이 최대 값보다 클 경우 validation 위반이 발생합니다.")
    public void searchUnitProfilesFailWithBothMinAndMaxAgesWithinLimitButWithMinGreaterThanMax() {
        bothAgesRequestWithinLimitButMinGreaterThanMax = SearchConditionsRequest.builder()
                .gender("M")
                .minAge(MAX_AGE_RANGE)
                .maxAge(MAX_AGE_RANGE - 1)
                .speakLanguage(KOREAN)
                .learningLanguage(ENGLISH)
                .learningLanguageLevel(levels)
                .build();

        Set<ConstraintViolation<SearchConditionsRequest>> violations = validator.validate(bothAgesRequestWithinLimitButMinGreaterThanMax);

        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsOnly(inappropriateAgeRangeExceptionMessage);
    }

    @Test
    @DisplayName("최대, 최소 나이를 모두 선택조건으로 지정해 다른 사용자를 검색할 때 설정된 최소, 최대 나이 조건이 모두 제한조건 범위 내에 존재하며 " +
            "최대 값이 최소 값보다 크거나 같을 경우 validation 검증을 통과합니다.")
    public void searchUnitProfilesSuccessWithBothMinAndMaxAgesWithinLimitAndWithMaxEqualOrGreaterThanMin() {
        bothAgesRequestWithinLimitAndMaxEqualOrGreaterThanMin = SearchConditionsRequest.builder()
                .gender("M")
                .minAge(MAX_AGE_RANGE)
                .maxAge(MAX_AGE_RANGE)
                .speakLanguage(KOREAN)
                .learningLanguage(ENGLISH)
                .learningLanguageLevel(levels)
                .build();

        Set<ConstraintViolation<SearchConditionsRequest>> violations = validator.validate(bothAgesRequestWithinLimitAndMaxEqualOrGreaterThanMin);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("필수 조건이 하나도 선택되지 않았고, 입력된 선택조건마저 정해진 범위를 벗어난 경우 모두 validation 위반을 일으킵니다.")
    public void searchUnitProfilesFailWithNoConditionsValid() {
        noneValidConditions = SearchConditionsRequest.builder()
                .gender("MFO")
                .minAge(belowMinAgeLimit)
                .maxAge(aboveMaxAgeLimit)
                .build();

        Set<ConstraintViolation<SearchConditionsRequest>> violations = validator.validate(noneValidConditions);

        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 5);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(inappropriateAgeRangeExceptionMessage, inappropriateGenderExceptionMessage, noSpeakLangExceptionMessage,
                        noLearningLangExceptionMessage, noLearningLangLevelsExceptionMessage);
    }

    @Test
    @DisplayName("필수 조건이 모두 선택되고, 입력된 선택조건도 정해진 범위 내에 존재하는 경우 모두 validation 검증을 통과합니다.")
    public void searchUnitProfilesSuccessWithAllConditionsValid() {
        allValidConditions = SearchConditionsRequest.builder()
                .gender("M")
                .minAge(MIN_AGE_RANGE)
                .maxAge(MAX_AGE_RANGE)
                .speakLanguage(KOREAN)
                .learningLanguage(ENGLISH)
                .learningLanguageLevel(levels)
                .build();

        Set<ConstraintViolation<SearchConditionsRequest>> violations = validator.validate(allValidConditions);

        assertTrue(violations.isEmpty());
    }
}
