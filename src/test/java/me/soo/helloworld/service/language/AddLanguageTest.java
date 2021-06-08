package me.soo.helloworld.service.language;

import me.soo.helloworld.enumeration.LanguageLevel;
import me.soo.helloworld.enumeration.LanguageStatus;
import me.soo.helloworld.exception.language.DuplicateLanguageException;
import me.soo.helloworld.exception.language.InvalidLanguageLevelException;
import me.soo.helloworld.exception.language.LanguageLimitExceededException;
import me.soo.helloworld.mapper.LanguageMapper;
import me.soo.helloworld.model.language.LanguageData;
import me.soo.helloworld.model.language.LanguageRequest;
import me.soo.helloworld.service.LanguageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static me.soo.helloworld.TestLanguages.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddLanguageTest {

    private final String userId = "Soo1045";

    private final LanguageStatus learning = LanguageStatus.LEARNING;

    private final LanguageStatus canSpeak = LanguageStatus.CAN_SPEAK;

    private final LanguageStatus nativeLang = LanguageStatus.NATIVE;

    @Mock
    LanguageMapper languageMapper;

    @InjectMocks
    LanguageService languageService;

    List<LanguageRequest> newNativeLangRequestWithTwo;

    List<LanguageRequest> newNativeLangRequestWithFour;

    List<LanguageRequest> newLearningLangRequestWithTwo;

    List<LanguageRequest> newLearningLangRequestWithFour;

    List<LanguageRequest> newLearningLangRequestWithEight;

    List<LanguageRequest> newCanSpeakLangRequestWithTwo;

    List<LanguageRequest> newCanSpeakLangRequestWithFour;

    List<LanguageData> existingLearningLangData;

    List<LanguageData> existingCanSpeakLangData;

    List<LanguageData> existingNativeLangData;

    List<LanguageRequest> duplicateInNewLearningLangRequest;

    List<LanguageRequest> duplicateInNewCanSpeakLangRequest;

    List<LanguageRequest> duplicateInNewNativeLangRequest;

    List<LanguageRequest> newLearningLangDuplicateWithExisting;

    List<LanguageRequest> newCanSpeakLangDuplicateWithExisting;

    List<LanguageRequest> newNativeLangDuplicateWithExisting;

    @BeforeEach
    public void setUp() {
        // 새로운 학습 언어 8개 추가
        newLearningLangRequestWithEight = new ArrayList<>();
        newLearningLangRequestWithEight.add(new LanguageRequest(ENGLISH, LanguageLevel.BEGINNER));
        newLearningLangRequestWithEight.add(new LanguageRequest(KOREAN, LanguageLevel.ELEMENTARY));
        newLearningLangRequestWithEight.add(new LanguageRequest(GERMAN, LanguageLevel.INTERMEDIATE));
        newLearningLangRequestWithEight.add(new LanguageRequest(PORTUGUESE, LanguageLevel.UPPER_INTERMEDIATE));
        newLearningLangRequestWithEight.add(new LanguageRequest(ARABIC, LanguageLevel.ADVANCED));
        newLearningLangRequestWithEight.add(new LanguageRequest(CHINESE_CANTONESE, LanguageLevel.PROFICIENCY));
        newLearningLangRequestWithEight.add(new LanguageRequest(FRENCH, LanguageLevel.BEGINNER));
        newLearningLangRequestWithEight.add(new LanguageRequest(CHINESE_MANDARIN, LanguageLevel.ELEMENTARY));

        // 새로운 학습 언어 2개 추가
        newLearningLangRequestWithTwo = new ArrayList<>();
        newLearningLangRequestWithTwo.add(new LanguageRequest(ENGLISH, LanguageLevel.BEGINNER));
        newLearningLangRequestWithTwo.add(new LanguageRequest(KOREAN, LanguageLevel.BEGINNER));

        // 새로운 학습 언어 4개 추가
        newLearningLangRequestWithFour = new ArrayList<>();
        newLearningLangRequestWithFour.add(new LanguageRequest(ENGLISH, LanguageLevel.INTERMEDIATE));
        newLearningLangRequestWithFour.add(new LanguageRequest(KOREAN, LanguageLevel.UPPER_INTERMEDIATE));
        newLearningLangRequestWithFour.add(new LanguageRequest(GERMAN, LanguageLevel.ADVANCED));
        newLearningLangRequestWithFour.add(new LanguageRequest(PORTUGUESE, LanguageLevel.PROFICIENCY));


        // 새로운 구사 언어 2개 추가
        newCanSpeakLangRequestWithTwo = new ArrayList<>();
        newCanSpeakLangRequestWithTwo.add(new LanguageRequest(ENGLISH, LanguageLevel.PROFICIENCY));
        newCanSpeakLangRequestWithTwo.add(new LanguageRequest(KOREAN, LanguageLevel.PROFICIENCY));

        // 새로운 구사 언어 4개 추가
        newCanSpeakLangRequestWithFour = new ArrayList<>();
        newCanSpeakLangRequestWithFour.add(new LanguageRequest(ENGLISH, LanguageLevel.PROFICIENCY));
        newCanSpeakLangRequestWithFour.add(new LanguageRequest(KOREAN, LanguageLevel.PROFICIENCY));
        newCanSpeakLangRequestWithFour.add(new LanguageRequest(GERMAN, LanguageLevel.PROFICIENCY));
        newCanSpeakLangRequestWithFour.add(new LanguageRequest(PORTUGUESE, LanguageLevel.PROFICIENCY));

        // 새로운 모국어 4개 추가
        newNativeLangRequestWithFour = new ArrayList<>();
        newNativeLangRequestWithFour.add(new LanguageRequest(ENGLISH, LanguageLevel.NATIVE));
        newNativeLangRequestWithFour.add(new LanguageRequest(KOREAN, LanguageLevel.NATIVE));
        newNativeLangRequestWithFour.add(new LanguageRequest(GERMAN, LanguageLevel.NATIVE));
        newNativeLangRequestWithFour.add(new LanguageRequest(PORTUGUESE, LanguageLevel.NATIVE));

        // 새로운 모국어 2개 추가
        newNativeLangRequestWithTwo = new ArrayList<>();
        newNativeLangRequestWithTwo.add(new LanguageRequest(ENGLISH, LanguageLevel.NATIVE));
        newNativeLangRequestWithTwo.add(new LanguageRequest(KOREAN, LanguageLevel.NATIVE));

        // 기존에 저장된 학습 언어 데이터 2개
        existingLearningLangData = new ArrayList<>();
        existingLearningLangData.add(new LanguageData(FRENCH, LanguageLevel.BEGINNER, LanguageStatus.LEARNING));
        existingLearningLangData.add(new LanguageData(SPANISH, LanguageLevel.BEGINNER, LanguageStatus.LEARNING));

        // 기존에 저장된 구사 언어 데이터 2개
        existingCanSpeakLangData = new ArrayList<>();
        existingCanSpeakLangData.add(new LanguageData(FRENCH, LanguageLevel.BEGINNER, LanguageStatus.CAN_SPEAK));
        existingCanSpeakLangData.add(new LanguageData(SPANISH, LanguageLevel.BEGINNER, LanguageStatus.CAN_SPEAK));

        // 기존에 저장된 모국어 데이터 2개
        existingNativeLangData = new ArrayList<>();
        existingNativeLangData.add(new LanguageData(FRENCH, LanguageLevel.NATIVE, LanguageStatus.NATIVE));
        existingNativeLangData.add(new LanguageData(SPANISH, LanguageLevel.NATIVE, LanguageStatus.NATIVE));

        // 새롭게 추가할 언어 2개, 요청 자체에 중복이 있는 경우
        duplicateInNewLearningLangRequest = new ArrayList<>();
        duplicateInNewLearningLangRequest.add(new LanguageRequest(ENGLISH, LanguageLevel.INTERMEDIATE));
        duplicateInNewLearningLangRequest.add(new LanguageRequest(ENGLISH, LanguageLevel.BEGINNER));

        // 새롭게 추가할 언어 2개, 요청 자체에 중복이 있는 경우
        duplicateInNewCanSpeakLangRequest = new ArrayList<>();
        duplicateInNewCanSpeakLangRequest.add(new LanguageRequest(ENGLISH, LanguageLevel.PROFICIENCY));
        duplicateInNewCanSpeakLangRequest.add(new LanguageRequest(ENGLISH, LanguageLevel.PROFICIENCY));

        // 새롭게 추가할 모국어 2개, 요청 자체에 중복이 있는 경우
        duplicateInNewNativeLangRequest = new ArrayList<>();
        duplicateInNewNativeLangRequest.add(new LanguageRequest(ENGLISH, LanguageLevel.NATIVE));
        duplicateInNewNativeLangRequest.add(new LanguageRequest(ENGLISH, LanguageLevel.NATIVE));

        // 새롭게 추가할 학습 언어 2개, 기존에 등록된 언어와 중복된 언어를 가진 경우
        newLearningLangDuplicateWithExisting = new ArrayList<>();
        newLearningLangDuplicateWithExisting.add(new LanguageRequest(ENGLISH, LanguageLevel.INTERMEDIATE));
        newLearningLangDuplicateWithExisting.add(new LanguageRequest(FRENCH, LanguageLevel.BEGINNER));

        // 새롭게 추가할 구사 언어 2개, 기존에 등록된 언어와 중복된 언어를 가진 경우
        newCanSpeakLangDuplicateWithExisting = new ArrayList<>();
        newCanSpeakLangDuplicateWithExisting.add(new LanguageRequest(ENGLISH, LanguageLevel.INTERMEDIATE));
        newCanSpeakLangDuplicateWithExisting.add(new LanguageRequest(FRENCH, LanguageLevel.BEGINNER));

        // 새롭게 추가할 모국어 2개, 기존에 등록된 언어와 중복된 언어를 가진 경우
        newNativeLangDuplicateWithExisting = new ArrayList<>();
        newNativeLangDuplicateWithExisting.add(new LanguageRequest(ENGLISH, LanguageLevel.NATIVE));
        newNativeLangDuplicateWithExisting.add(new LanguageRequest(FRENCH, LanguageLevel.NATIVE));

    }

    /*
        LanguageStatus.LEARNING 에 대한 테스트를
        담고있는 부분입니다.
     */

    @Test
    @DisplayName("중복, 레벨설정에 대한 문제 없이, 해당 언어 status 의 limit(8) 수를 넘지 않는 언어를 새롭게 추가할 경우 테스트에 성공합니다. - DB에 기존 데이터 없음")
    public void addLearningLanguageSuccessUnderLimit() {
        when(languageMapper.getLanguages(userId)).thenReturn(new ArrayList<>(0));
        doNothing().when(languageMapper).insertLanguages(userId, newLearningLangRequestWithEight, learning);

        languageService.addLanguages(userId, newLearningLangRequestWithEight, learning);

        verify(languageMapper, times(1)).getLanguages(userId);
        verify(languageMapper, times(1)).insertLanguages(userId, newLearningLangRequestWithEight, learning);
    }

    @Test
    @DisplayName("중복, 레벨설정에 대한 문제 없이, DB에 등록되 있는 언어를 포함해, 해당 언어 status 의 총 limit(8) 수를 넘지 않는 언어를 추가할 경우 테스트에 성공합니다.")
    public void addLearningLanguageSuccessUnderLimitWithDBData() {
        when(languageMapper.getLanguages(userId)).thenReturn(existingLearningLangData);
        doNothing().when(languageMapper).insertLanguages(userId, newLearningLangRequestWithFour, learning);

        languageService.addLanguages(userId, newLearningLangRequestWithFour, learning);

        verify(languageMapper, times(1)).getLanguages(userId);
        verify(languageMapper, times(1)).insertLanguages(userId, newLearningLangRequestWithFour, learning);
    }

    @Test
    @DisplayName("DB에 저장된 정보가 없어도 추가할 언어의 수가 해당 언어 status 의 limit(8) 를 초과할 경우 LanguageLimitExceededException 가 발생하며 테스트에 실패합니다.")
    public void addLearningLanguageFailOverLimit() {
        when(languageMapper.getLanguages(userId)).thenReturn(new ArrayList<>(0));
        newLearningLangRequestWithEight.add(new LanguageRequest(RUSSIAN, LanguageLevel.ADVANCED));

        assertThrows(LanguageLimitExceededException.class, () -> {
            languageService.addLanguages(userId, newLearningLangRequestWithEight, learning);
        });

        verify(languageMapper, times(1)).getLanguages(userId);
        verify(languageMapper, never()).insertLanguages(userId, newLearningLangRequestWithFour, learning);
    }

    @Test
    @DisplayName("DB에 저장된 정보를 포함해 해당 언어 status 의 limit(8) 을 초과하는 숫자의 언어를 추가할 경우 LanguageLimitExceededException 가 발생하며 테스트에 실패합니다.")
    public void addLearningLanguageFailOverLimitWithDBData() {
        when(languageMapper.getLanguages(userId)).thenReturn(existingLearningLangData);

        assertThrows(LanguageLimitExceededException.class, () -> {
            languageService.addLanguages(userId, newLearningLangRequestWithEight, learning);
        });

        verify(languageMapper, times(1)).getLanguages(userId);
        verify(languageMapper, never()).insertLanguages(userId, newLearningLangRequestWithFour, learning);
    }

    @Test
    @DisplayName("LEARNING status 의 언어에 대해 NATIVE 로 레벨 설정이 되어 추가 요청이 들어오면 InvalidLanguageLevelException 이 발생하며 테스트에 실패합니다.")
    public void addLearningLanguageFailWithInvalidLanguageLevel() {
        when(languageMapper.getLanguages(userId)).thenReturn(existingLearningLangData);

        List<LanguageRequest> newLangWithInappropriateLevel = new ArrayList<>();
        newLangWithInappropriateLevel.add(new LanguageRequest(ENGLISH, LanguageLevel.NATIVE));

        assertThrows(InvalidLanguageLevelException.class, () -> {
            languageService.addLanguages(userId, newLangWithInappropriateLevel, learning);
        });

        verify(languageMapper, times(1)).getLanguages(userId);
        verify(languageMapper, never()).insertLanguages(userId, newLearningLangRequestWithFour, learning);
    }

    @Test
    @DisplayName("새롭게 추가 요청을 보낸 언어들에 중복이 있는 경우 DuplicateLanguageException 이 발생하며 테스트에 실패합니다.")
    public void addLearningLanguageFailWithDuplicateInNewLangData() {
        when(languageMapper.getLanguages(userId)).thenReturn(existingLearningLangData);

        assertThrows(DuplicateLanguageException.class, () -> {
            languageService.addLanguages(userId, duplicateInNewLearningLangRequest, learning);
        });

        verify(languageMapper, times(1)).getLanguages(userId);
        verify(languageMapper, never()).insertLanguages(userId, newLearningLangRequestWithFour, learning);
    }

    @Test
    @DisplayName("새롭게 추가 요청을 보낸 언어 중 기존에 추가된 언어와 중복이 있는 경우 DuplicateLanguageException 이 발생하며 테스트에 실패합니다.")
    public void addLearningLanguageFailWithDuplicateExistingLangData() {
        when(languageMapper.getLanguages(userId)).thenReturn(existingLearningLangData);

        assertThrows(DuplicateLanguageException.class, () -> {
            languageService.addLanguages(userId, newLearningLangDuplicateWithExisting, learning);
        });

        verify(languageMapper, times(1)).getLanguages(userId);
        verify(languageMapper, never()).insertLanguages(userId, newLearningLangRequestWithFour, learning);
    }

    /*
        LanguageStatus.CAN_SPEAK 에 대한 테스트를
        담고있는 부분입니다.
     */

    @Test
    @DisplayName("중복, 레벨설정에 대한 문제 없이, 해당 언어 status 의 limit(4) 수를 넘지 않는 언어를 새롭게 추가할 경우 테스트에 성공합니다. - DB에 기존 데이터 없음")
    public void addCanSpeakLanguageSuccessUnderLimit() {
        when(languageMapper.getLanguages(userId)).thenReturn(new ArrayList<>(0));
        doNothing().when(languageMapper).insertLanguages(userId, newCanSpeakLangRequestWithFour, canSpeak);

        languageService.addLanguages(userId, newCanSpeakLangRequestWithFour, canSpeak);

        verify(languageMapper, times(1)).getLanguages(userId);
        verify(languageMapper, times(1)).insertLanguages(userId, newCanSpeakLangRequestWithFour, canSpeak);
    }

    @Test
    @DisplayName("중복, 레벨설정에 대한 문제 없이, DB에 등록되 있는 언어를 포함해, 해당 언어 status 의 총 limit(4) 수를 넘지 않는 언어를 추가할 경우 테스트에 성공합니다.")
    public void addCanSpeakLanguageSuccessUnderLimitWithDBData() {
        when(languageMapper.getLanguages(userId)).thenReturn(existingCanSpeakLangData);
        doNothing().when(languageMapper).insertLanguages(userId, newCanSpeakLangRequestWithTwo, canSpeak);

        languageService.addLanguages(userId, newCanSpeakLangRequestWithTwo, canSpeak);

        verify(languageMapper, times(1)).getLanguages(userId);
        verify(languageMapper, times(1)).insertLanguages(userId, newCanSpeakLangRequestWithTwo, canSpeak);
    }

    @Test
    @DisplayName("DB에 저장된 정보가 없어도 추가할 언어의 수가 해당 언어 status 의 limit(4) 를 초과할 경우 LanguageLimitExceededException 가 발생하며 테스트에 실패합니다.")
    public void addCanSpeakLanguageFailOverLimit() {
        when(languageMapper.getLanguages(userId)).thenReturn(new ArrayList<>(0));

        // 이미 4개를 담고 있는 새로운 언어 데이터 리스트에 예외 처리를 위해 1개 더 추가
        newCanSpeakLangRequestWithFour.add(new LanguageRequest(JAPANESE, LanguageLevel.PROFICIENCY));

        assertThrows(LanguageLimitExceededException.class, () -> {
            languageService.addLanguages(userId, newCanSpeakLangRequestWithFour, canSpeak);
        });

        verify(languageMapper, times(1)).getLanguages(userId);
        verify(languageMapper, never()).insertLanguages(userId, newCanSpeakLangRequestWithFour, canSpeak);
    }

    @Test
    @DisplayName("DB에 저장된 정보를 포함해 해당 언어 status 의 limit(4) 을 초과하는 숫자의 언어를 추가할 경우 LanguageLimitExceededException 가 발생하며 테스트에 실패합니다.")
    public void addCanSpeakLanguageFailOverLimitWithDBData() {
        when(languageMapper.getLanguages(userId)).thenReturn(existingCanSpeakLangData);

        assertThrows(LanguageLimitExceededException.class, () -> {
            languageService.addLanguages(userId, newCanSpeakLangRequestWithFour, canSpeak);
        });

        verify(languageMapper, times(1)).getLanguages(userId);
        verify(languageMapper, never()).insertLanguages(userId, newCanSpeakLangRequestWithFour, canSpeak);
    }

    @Test
    @DisplayName("CAN_SPEAK status 의 언어에 대해 NATIVE 로 레벨 설정이 되어 추가 요청이 들어오면 InvalidLanguageLevelException 이 발생하며 테스트에 실패합니다.")
    public void addCanSpeakLanguageFailWithInvalidLanguageLevel() {
        when(languageMapper.getLanguages(userId)).thenReturn(existingCanSpeakLangData);

        List<LanguageRequest> newLangWithInappropriateLevel = new ArrayList<>();
        newLangWithInappropriateLevel.add(new LanguageRequest(ENGLISH, LanguageLevel.NATIVE));
        newLangWithInappropriateLevel.add(new LanguageRequest(PORTUGUESE, LanguageLevel.PROFICIENCY));

        assertThrows(InvalidLanguageLevelException.class, () -> {
            languageService.addLanguages(userId, newLangWithInappropriateLevel, canSpeak);
        });

        verify(languageMapper, times(1)).getLanguages(userId);
        verify(languageMapper, never()).insertLanguages(userId, newCanSpeakLangRequestWithFour, canSpeak);
    }

    @Test
    @DisplayName("새롭게 추가 요청을 보낸 언어들에 중복이 있는 경우 DuplicateLanguageException 이 발생하며 테스트에 실패합니다.")
    public void addCanSpeakLanguageFailWithDuplicateInNewLangData() {
        when(languageMapper.getLanguages(userId)).thenReturn(existingCanSpeakLangData);

        assertThrows(DuplicateLanguageException.class, () -> {
            languageService.addLanguages(userId, duplicateInNewCanSpeakLangRequest, canSpeak);
        });

        verify(languageMapper, times(1)).getLanguages(userId);
        verify(languageMapper, never()).insertLanguages(userId, newCanSpeakLangRequestWithFour, canSpeak);
    }

    @Test
    @DisplayName("새롭게 추가 요청을 보낸 언어 중 기존에 추가된 언어와 중복이 있는 경우 DuplicateLanguageException 이 발생하며 테스트에 실패합니다.")
    public void addCanSpeakLanguageFailWithDuplicateExistingLangData() {
        when(languageMapper.getLanguages(userId)).thenReturn(existingCanSpeakLangData);

        assertThrows(DuplicateLanguageException.class, () -> {
            languageService.addLanguages(userId, duplicateInNewCanSpeakLangRequest, canSpeak);
        });

        verify(languageMapper, times(1)).getLanguages(userId);
        verify(languageMapper, never()).insertLanguages(userId, duplicateInNewCanSpeakLangRequest, canSpeak);
    }

    /*
        LanguageStatus.NATIVE 에 대한 테스트를
        담고있는 부분입니다.
     */

    @Test
    @DisplayName("중복, 레벨설정에 대한 문제 없이, 해당 언어 status 의 limit(4) 수를 넘지 않는 언어를 추가할 경우 테스트에 성공합니다. - NATIVE/DB에 저장된 정보 또한 존재하지 않을 때")
    public void addNativeLanguageSuccessUnderLimit() {
        when(languageMapper.getLanguages(userId)).thenReturn(new ArrayList<>(0));
        doNothing().when(languageMapper).insertLanguages(userId, newNativeLangRequestWithFour, nativeLang);

        languageService.addLanguages(userId, newNativeLangRequestWithFour, nativeLang);

        verify(languageMapper, times(1)).getLanguages(userId);
        verify(languageMapper, times(1)).insertLanguages(userId, newNativeLangRequestWithFour, nativeLang);
    }

    @Test
    @DisplayName("중복, 레벨설정에 대한 문제 없이, DB에 등록되 있는 언어를 포함해, 해당 언어 status 의 총 limit(4) 수를 넘지 않는 언어를 추가할 경우 테스트에 성공합니다.")
    public void addNativeLanguageSuccessUnderLimitWithDBData() {
        when(languageMapper.getLanguages(userId)).thenReturn(existingLearningLangData);
        doNothing().when(languageMapper).insertLanguages(userId, newNativeLangRequestWithTwo, nativeLang);

        languageService.addLanguages(userId, newNativeLangRequestWithTwo, nativeLang);

        verify(languageMapper, times(1)).getLanguages(userId);
        verify(languageMapper, times(1)).insertLanguages(userId, newNativeLangRequestWithTwo, nativeLang);
    }

    @Test
    @DisplayName("DB에 저장된 정보가 없어도 추가할 언어의 수가 해당 언어 status 의 limit(4) 를 초과할 경우 LanguageLimitExceededException 가 발생하며 테스트에 실패합니다.")
    public void addNativeLanguageFailOverLimit() {
        when(languageMapper.getLanguages(userId)).thenReturn(new ArrayList<>(0));

        // 이미 4개를 담고 있는 새로운 언어 데이터 리스트에 예외 처리를 위해 모국어를 1개 더 추가
        newNativeLangRequestWithFour.add(new LanguageRequest(JAPANESE, LanguageLevel.NATIVE));

        assertThrows(LanguageLimitExceededException.class, () -> {
            languageService.addLanguages(userId, newNativeLangRequestWithFour, nativeLang);
        });

        verify(languageMapper, times(1)).getLanguages(userId);
        verify(languageMapper, never()).insertLanguages(userId, newNativeLangRequestWithFour, nativeLang);
    }

    @Test
    @DisplayName("DB에 저장된 정보를 포함해 해당 언어 status 의 limit(4) 을 초과하는 숫자의 언어를 추가할 경우 LanguageLimitExceededException 가 발생하며 테스트에 실패합니다.")
    public void addNativeLanguageFailOverLimitWithDBData() {
        when(languageMapper.getLanguages(userId)).thenReturn(existingNativeLangData);

        assertThrows(LanguageLimitExceededException.class, () -> {
            languageService.addLanguages(userId, newNativeLangRequestWithFour, nativeLang);
        });

        verify(languageMapper, times(1)).getLanguages(userId);
        verify(languageMapper, never()).insertLanguages(userId, newNativeLangRequestWithFour, nativeLang);
    }

    @Test
    @DisplayName("NATIVE status 의 언어에 대해 NATIVE 가 아닌 다른 언어레벨 설정으로 추가 요청이 들어오면 InvalidLanguageLevelException 이 발생하며 테스트에 실패합니다.")
    public void addNativeLanguageFailWithInvalidLanguageLevel() {
        when(languageMapper.getLanguages(userId)).thenReturn(existingNativeLangData);

        List<LanguageRequest> newNativeLangWithInappropriateLevel = new ArrayList<>();
        newNativeLangWithInappropriateLevel.add(new LanguageRequest(ENGLISH, LanguageLevel.BEGINNER));
        newNativeLangWithInappropriateLevel.add(new LanguageRequest(KOREAN, LanguageLevel.NATIVE));

        assertThrows(InvalidLanguageLevelException.class, () -> {
            languageService.addLanguages(userId, newNativeLangWithInappropriateLevel, nativeLang);
        });

        verify(languageMapper, times(1)).getLanguages(userId);
        verify(languageMapper, never()).insertLanguages(userId, newNativeLangWithInappropriateLevel, nativeLang);
    }

    @Test
    @DisplayName("새롭게 추가 요청을 보낸 언어들에 중복이 있는 경우 DuplicateLanguageException 이 발생하며 테스트에 실패합니다.")
    public void addNativeLanguageFailWithDuplicateInNewLangData() {
        when(languageMapper.getLanguages(userId)).thenReturn(existingNativeLangData);

        assertThrows(DuplicateLanguageException.class, () -> {
            languageService.addLanguages(userId, duplicateInNewNativeLangRequest, nativeLang);
        });

        verify(languageMapper, times(1)).getLanguages(userId);
        verify(languageMapper, never()).insertLanguages(userId, duplicateInNewNativeLangRequest, nativeLang);
    }

    @Test
    @DisplayName("새롭게 추가 요청을 보낸 언어 중 기존에 추가된 언어와 중복이 있는 경우 DuplicateLanguageException 이 발생하며 테스트에 실패합니다.")
    public void addNativeLanguageFailWithDuplicateExistingLangData() {
        when(languageMapper.getLanguages(userId)).thenReturn(existingNativeLangData);

        assertThrows(DuplicateLanguageException.class, () -> {
            languageService.addLanguages(userId, newNativeLangDuplicateWithExisting, nativeLang);
        });

        verify(languageMapper, times(1)).getLanguages(userId);
        verify(languageMapper, never()).insertLanguages(userId, newNativeLangDuplicateWithExisting, nativeLang);
    }
}
