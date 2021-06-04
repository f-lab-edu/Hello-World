package me.soo.helloworld.integration;

import me.soo.helloworld.enumeration.LanguageLevel;
import me.soo.helloworld.enumeration.LanguageStatus;
import me.soo.helloworld.exception.language.InvalidLanguageLevelException;
import me.soo.helloworld.model.language.LanguageRequest;
import me.soo.helloworld.service.LanguageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static me.soo.helloworld.TestLanguages.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ITModifyLevel {

    private final String userId = "Soo1045";

    @Autowired
    LanguageService languageService;

    List<LanguageRequest> learningLang;

    List<LanguageRequest> canSpeakLang;

    List<LanguageRequest> nativeLang;

    List<LanguageRequest> learningLangValidLevelSet;

    List<LanguageRequest> canSpeakLangValidLevelSet;

    List<LanguageRequest> learningLangInvalidLevelSet;

    List<LanguageRequest> canSpeakLangInValidLevelSet;


    @BeforeEach
    public void setUp() {
        learningLang = new ArrayList<>();
        learningLang.add(new LanguageRequest(KOREAN, LanguageLevel.BEGINNER));
        learningLang.add(new LanguageRequest(ENGLISH, LanguageLevel.BEGINNER));
        learningLang.add(new LanguageRequest(FRENCH, LanguageLevel.BEGINNER));
        learningLang.add(new LanguageRequest(SPANISH, LanguageLevel.BEGINNER));

        canSpeakLang = new ArrayList<>();
        canSpeakLang.add(new LanguageRequest(RUSSIAN, LanguageLevel.BEGINNER));
        canSpeakLang.add(new LanguageRequest(GERMAN, LanguageLevel.BEGINNER));
        canSpeakLang.add(new LanguageRequest(ITALIAN, LanguageLevel.BEGINNER));
        canSpeakLang.add(new LanguageRequest(CHINESE_CANTONESE, LanguageLevel.BEGINNER));

        nativeLang = new ArrayList<>();
        nativeLang.add(new LanguageRequest(RUSSIAN, LanguageLevel.NATIVE));
        nativeLang.add(new LanguageRequest(GERMAN, LanguageLevel.NATIVE));
        nativeLang.add(new LanguageRequest(ITALIAN, LanguageLevel.NATIVE));
        nativeLang.add(new LanguageRequest(CHINESE_CANTONESE, LanguageLevel.NATIVE));

        learningLangValidLevelSet = new ArrayList<>();
        learningLangValidLevelSet.add(new LanguageRequest(KOREAN, LanguageLevel.ADVANCED));
        learningLangValidLevelSet.add(new LanguageRequest(ENGLISH, LanguageLevel.PROFICIENCY));
        learningLangValidLevelSet.add(new LanguageRequest(FRENCH, LanguageLevel.UPPER_INTERMEDIATE));
        learningLangValidLevelSet.add(new LanguageRequest(SPANISH, LanguageLevel.ELEMENTARY));

        canSpeakLangValidLevelSet = new ArrayList<>();
        canSpeakLangValidLevelSet.add(new LanguageRequest(RUSSIAN, LanguageLevel.ADVANCED));
        canSpeakLangValidLevelSet.add(new LanguageRequest(GERMAN, LanguageLevel.PROFICIENCY));
        canSpeakLangValidLevelSet.add(new LanguageRequest(ITALIAN, LanguageLevel.UPPER_INTERMEDIATE));
        canSpeakLangValidLevelSet.add(new LanguageRequest(CHINESE_CANTONESE, LanguageLevel.ELEMENTARY));

        learningLangInvalidLevelSet = new ArrayList<>();
        learningLangInvalidLevelSet.add(new LanguageRequest(KOREAN, LanguageLevel.ADVANCED));
        learningLangInvalidLevelSet.add(new LanguageRequest(ENGLISH, LanguageLevel.PROFICIENCY));
        learningLangInvalidLevelSet.add(new LanguageRequest(FRENCH, LanguageLevel.UPPER_INTERMEDIATE));
        learningLangInvalidLevelSet.add(new LanguageRequest(SPANISH, LanguageLevel.NATIVE));

        canSpeakLangInValidLevelSet = new ArrayList<>();
        canSpeakLangInValidLevelSet.add(new LanguageRequest(RUSSIAN, LanguageLevel.ADVANCED));
        canSpeakLangInValidLevelSet.add(new LanguageRequest(GERMAN, LanguageLevel.PROFICIENCY));
        canSpeakLangInValidLevelSet.add(new LanguageRequest(ITALIAN, LanguageLevel.UPPER_INTERMEDIATE));
        canSpeakLangInValidLevelSet.add(new LanguageRequest(CHINESE_CANTONESE, LanguageLevel.NATIVE));
    }

    @Test
    @DisplayName("추가된 언어 Status 가 Native 가 아닌 언어들에 대한 언어 레벨 변경이 이루어질 경우 테스트에 성공합니다.")
    public void learningLangModifyLevelSuccess() {
        languageService.addLanguages(userId, learningLang, LanguageStatus.LEARNING);
        languageService.addLanguages(userId, canSpeakLang, LanguageStatus.CAN_SPEAK);

        languageService.modifyLanguageLevels(userId, learningLangValidLevelSet, LanguageStatus.LEARNING);
        languageService.modifyLanguageLevels(userId, canSpeakLangValidLevelSet, LanguageStatus.CAN_SPEAK);

        List<LanguageRequest> languageList = languageService.getLanguages(userId);

        Map<Integer, LanguageLevel> languageMap = languageList.stream()
                .collect(Collectors.toMap(LanguageRequest::getId, LanguageRequest::getLevel));

        assertTrue(learningLangValidLevelSet.stream().allMatch(languageData -> languageMap.get(languageData.getId()).equals(languageData.getLevel())));
        assertTrue(canSpeakLangValidLevelSet.stream().allMatch(languageData -> languageMap.get(languageData.getId()).equals(languageData.getLevel())));
    }

    @Test
    @DisplayName("언어레벨 변경 요청으로 들어온 레벨 값들 중 모국어(NATIVE) LEVEL 이 들어있으면 InvalidLanguageLevelException 이 발생하며 테스트에 실패합니다.")
    public void learningLangModifyLevelFailWithNativeLangStatus() {
        languageService.addLanguages(userId, learningLang, LanguageStatus.LEARNING);
        languageService.addLanguages(userId, canSpeakLang, LanguageStatus.CAN_SPEAK);

        assertThrows(InvalidLanguageLevelException.class, () -> {
            languageService.modifyLanguageLevels(userId, learningLangInvalidLevelSet, LanguageStatus.LEARNING);
        });

        assertThrows(InvalidLanguageLevelException.class, () -> {
            languageService.modifyLanguageLevels(userId, canSpeakLangInValidLevelSet, LanguageStatus.CAN_SPEAK);
        });
    }

    @Test
    @DisplayName("언어 Status 가 모국어(Native) 로 등록된 언어에 대해 레벨 변경 요청이 들어오면 InvalidLanguageLevelException 이 발생하며 테스트에 실패합니다.")
    public void canSpeakLangModifyLevelSuccess() {
        languageService.addLanguages(userId, nativeLang, LanguageStatus.NATIVE);

        assertThrows(InvalidLanguageLevelException.class, () -> {
            languageService.modifyLanguageLevels(userId, nativeLang, LanguageStatus.NATIVE);
        });
    }
}
