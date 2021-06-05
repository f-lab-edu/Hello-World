package me.soo.helloworld.service.language;

import me.soo.helloworld.enumeration.LanguageLevel;
import me.soo.helloworld.enumeration.LanguageStatus;
import me.soo.helloworld.exception.language.InvalidLanguageLevelException;
import me.soo.helloworld.mapper.LanguageMapper;
import me.soo.helloworld.model.language.LanguageRequest;
import me.soo.helloworld.service.LanguageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static me.soo.helloworld.TestLanguages.*;
import static me.soo.helloworld.TestLanguages.PORTUGUESE;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ModifyLanguageTest {

    private String userId = "msugo1";

    @InjectMocks
    LanguageService languageService;

    @Mock
    LanguageMapper languageMapper;

    List<LanguageRequest> LevelsInvalid;

    /*
        1. status - native
            status - can_speak

        2. learning - native

        3. 성공
     */

    @Test
    @DisplayName("언어 상태(status)가 NATIVE 인 경우 InvalidLanguageLevelException 이 발생하며 언어 레벨 변경에 실패합니다.")
    public void modifyLevelFailWithNativeLanguageStatus() {
        List<LanguageRequest> levelsValid = new ArrayList<>();
        levelsValid.add(new LanguageRequest(ENGLISH, LanguageLevel.NATIVE));
        levelsValid.add(new LanguageRequest(KOREAN, LanguageLevel.NATIVE));
        levelsValid.add(new LanguageRequest(GERMAN, LanguageLevel.NATIVE));
        levelsValid.add(new LanguageRequest(PORTUGUESE, LanguageLevel.NATIVE));

        assertThrows(InvalidLanguageLevelException.class, () -> {
            languageService.modifyLevels(userId, levelsValid, LanguageStatus.NATIVE);
        });

        verify(languageMapper, never()).updateLevels(userId, levelsValid, LanguageStatus.NATIVE);
    }

    @Test
    @DisplayName("언어 상태(status)가 CAN_SPEAK 인 경우 InvalidLanguageLevelException 이 발생하며 언어 레벨 변경에 실패합니다.")
    public void modifyLevelFailWithCanSpeakLanguageStatus() {
        List<LanguageRequest> levelsValid = new ArrayList<>();
        levelsValid.add(new LanguageRequest(ENGLISH, LanguageLevel.PROFICIENCY));
        levelsValid.add(new LanguageRequest(KOREAN, LanguageLevel.PROFICIENCY));
        levelsValid.add(new LanguageRequest(GERMAN, LanguageLevel.PROFICIENCY));
        levelsValid.add(new LanguageRequest(PORTUGUESE, LanguageLevel.PROFICIENCY));

        assertThrows(InvalidLanguageLevelException.class, () -> {
            languageService.modifyLevels(userId, levelsValid, LanguageStatus.CAN_SPEAK);
        });

        verify(languageMapper, never()).updateLevels(userId, levelsValid, LanguageStatus.CAN_SPEAK);
    }

    @Test
    @DisplayName("언어 상태(status)가 Leaning 이더라도 변경할 언어 레벨에 NATIVE 가 포함되어 있다면 InvalidLanguageLevelException 이 발생하며 언어 레벨 변경에 실패합니다.")
    public void modifyLevelFailLearningLanguageStatusWithInvalidLevel() {
        List<LanguageRequest> levelsValid = new ArrayList<>();
        levelsValid.add(new LanguageRequest(ENGLISH, LanguageLevel.NATIVE));
        levelsValid.add(new LanguageRequest(KOREAN, LanguageLevel.PROFICIENCY));
        levelsValid.add(new LanguageRequest(GERMAN, LanguageLevel.PROFICIENCY));
        levelsValid.add(new LanguageRequest(PORTUGUESE, LanguageLevel.PROFICIENCY));

        assertThrows(InvalidLanguageLevelException.class, () -> {
            languageService.modifyLevels(userId, levelsValid, LanguageStatus.LEARNING);
        });

        verify(languageMapper, never()).updateLevels(userId, levelsValid, LanguageStatus.LEARNING);
    }

    @Test
    @DisplayName("언어 상태(status)가 Leaning 이고 변경할 언어 레벨에 NATIVE 가 포함되지 않았다면 언어 레벨 변경에 성공합니다.")
    public void modifyLevelSuccessLearningLanguageStatusWithValidLevel() {
        List<LanguageRequest> levelsValid = new ArrayList<>();
        levelsValid.add(new LanguageRequest(ENGLISH, LanguageLevel.ADVANCED));
        levelsValid.add(new LanguageRequest(KOREAN, LanguageLevel.PROFICIENCY));
        levelsValid.add(new LanguageRequest(GERMAN, LanguageLevel.BEGINNER));
        levelsValid.add(new LanguageRequest(PORTUGUESE, LanguageLevel.ELEMENTARY));

        languageService.modifyLevels(userId, levelsValid, LanguageStatus.LEARNING);

        verify(languageMapper, times(1)).updateLevels(userId, levelsValid, LanguageStatus.LEARNING);
    }

}
