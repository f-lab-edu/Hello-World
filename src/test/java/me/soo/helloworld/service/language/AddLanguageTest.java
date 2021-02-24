package me.soo.helloworld.service.language;

import me.soo.helloworld.enumeration.LanguageLevel;
import me.soo.helloworld.enumeration.LanguageStatus;
import me.soo.helloworld.exception.DuplicateLanguageException;
import me.soo.helloworld.exception.InvalidLanguageLevelException;
import me.soo.helloworld.exception.LanguageLimitExceededException;
import me.soo.helloworld.mapper.LanguageMapper;
import me.soo.helloworld.model.language.LanguageData;
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

    List<LanguageData> newNativeLangRequestWithTwo;

    List<LanguageData> newNativeLangRequestWithFour;

    List<LanguageData> newLangRequestWithTwo;

    List<LanguageData> newLangRequestWithFour;

    List<LanguageData> newLangRequestWithEight;

    List<LanguageData> existingLangData;

    List<LanguageData> existingNativeLangData;

    List<LanguageData> duplicateInNewLangRequest;

    List<LanguageData> duplicateInNewNativeLangRequest;

    List<LanguageData> newLangDuplicateWithExisting;

    List<LanguageData> newNativeLangDuplicateWithExisting;

    @BeforeEach
    public void setUp() {
        // 새로운 언어 8개 추가
        newLangRequestWithEight = new ArrayList<>();
        newLangRequestWithEight.add(new LanguageData(TestLangId.English, LanguageLevel.BEGINNER));
        newLangRequestWithEight.add(new LanguageData(TestLangId.KOREAN, LanguageLevel.ELEMENTARY));
        newLangRequestWithEight.add(new LanguageData(TestLangId.GERMAN, LanguageLevel.INTERMEDIATE));
        newLangRequestWithEight.add(new LanguageData(TestLangId.PORTUGUESE, LanguageLevel.UPPER_INTERMEDIATE));
        newLangRequestWithEight.add(new LanguageData(TestLangId.ARABIC, LanguageLevel.ADVANCED));
        newLangRequestWithEight.add(new LanguageData(TestLangId.CHINESE_CANTONESE, LanguageLevel.PROFICIENCY));
        newLangRequestWithEight.add(new LanguageData(TestLangId.FRENCH, LanguageLevel.BEGINNER));
        newLangRequestWithEight.add(new LanguageData(TestLangId.CHINESE_MANDARIN, LanguageLevel.ELEMENTARY));

        // 새로운 언어 2개 추가
        newLangRequestWithTwo = new ArrayList<>();
        newLangRequestWithTwo.add(new LanguageData(TestLangId.English, LanguageLevel.BEGINNER));
        newLangRequestWithTwo.add(new LanguageData(TestLangId.KOREAN, LanguageLevel.BEGINNER));

        // 새로운 언어 4개 추가
        newLangRequestWithFour = new ArrayList<>();
        newLangRequestWithFour.add(new LanguageData(TestLangId.English, LanguageLevel.INTERMEDIATE));
        newLangRequestWithFour.add(new LanguageData(TestLangId.KOREAN, LanguageLevel.UPPER_INTERMEDIATE));
        newLangRequestWithFour.add(new LanguageData(TestLangId.GERMAN, LanguageLevel.ADVANCED));
        newLangRequestWithFour.add(new LanguageData(TestLangId.PORTUGUESE, LanguageLevel.PROFICIENCY));

        // 새로운 모국어 4개 추가
        newNativeLangRequestWithFour = new ArrayList<>();
        newNativeLangRequestWithFour.add(new LanguageData(TestLangId.English, LanguageLevel.NATIVE));
        newNativeLangRequestWithFour.add(new LanguageData(TestLangId.KOREAN, LanguageLevel.NATIVE));
        newNativeLangRequestWithFour.add(new LanguageData(TestLangId.GERMAN, LanguageLevel.NATIVE));
        newNativeLangRequestWithFour.add(new LanguageData(TestLangId.PORTUGUESE, LanguageLevel.NATIVE));

        // 새로운 모국어 2개 추가
        newNativeLangRequestWithTwo = new ArrayList<>();
        newNativeLangRequestWithTwo.add(new LanguageData(TestLangId.English, LanguageLevel.NATIVE));
        newNativeLangRequestWithTwo.add(new LanguageData(TestLangId.KOREAN, LanguageLevel.NATIVE));

        // 기존에 저장된 언어 데이터 2개
        existingLangData = new ArrayList<>();
        existingLangData.add(new LanguageData(TestLangId.FRENCH, LanguageLevel.BEGINNER));
        existingLangData.add(new LanguageData(TestLangId.SPANISH, LanguageLevel.BEGINNER));

        // 기존에 저장된 모국어 데이터 2개
        existingNativeLangData = new ArrayList<>();
        existingNativeLangData.add(new LanguageData(TestLangId.FRENCH, LanguageLevel.NATIVE));
        existingNativeLangData.add(new LanguageData(TestLangId.SPANISH, LanguageLevel.NATIVE));

        // 새롭게 추가할 언어 2개, 요청 자체에 중복이 있는 경우
        duplicateInNewLangRequest = new ArrayList<>();
        duplicateInNewLangRequest.add(new LanguageData(TestLangId.English, LanguageLevel.INTERMEDIATE));
        duplicateInNewLangRequest.add(new LanguageData(TestLangId.English, LanguageLevel.BEGINNER));

        // 새롭게 추가할 모국어 2개, 요청 자체에 중복이 있는 경우
        duplicateInNewNativeLangRequest = new ArrayList<>();
        duplicateInNewNativeLangRequest.add(new LanguageData(TestLangId.English, LanguageLevel.NATIVE));
        duplicateInNewNativeLangRequest.add(new LanguageData(TestLangId.English, LanguageLevel.NATIVE));

        // 새롭게 추가할 언어 2개, 기존에 등록된 언어와 중복된 언어를 가진 경우
        newLangDuplicateWithExisting = new ArrayList<>();
        newLangDuplicateWithExisting.add(new LanguageData(TestLangId.English, LanguageLevel.INTERMEDIATE));
        newLangDuplicateWithExisting.add(new LanguageData(TestLangId.FRENCH, LanguageLevel.BEGINNER));

        newNativeLangDuplicateWithExisting = new ArrayList<>();
        newNativeLangDuplicateWithExisting.add(new LanguageData(TestLangId.English, LanguageLevel.NATIVE));
        newNativeLangDuplicateWithExisting.add(new LanguageData(TestLangId.FRENCH, LanguageLevel.NATIVE));

    }

    /*
        LanguageStatus.LEARNING 에 대한 테스트를
        담고있는 부분입니다.
     */

    @Test
    @DisplayName("중복, 레벨설정에 대한 문제 없이, 해당 언어 status 의 limit(8) 수를 넘지 않는 언어를 새롭게 추가할 경우 테스트에 성공합니다. - DB에 기존 데이터 없음")
    public void addLearningLanguageSuccessUnderLimit() {
        when(languageMapper.countLanguages(userId, learning)).thenReturn(0);
        when(languageMapper.getLanguages(userId)).thenReturn(new ArrayList<>(0));
        doNothing().when(languageMapper).insertLanguages(userId, newLangRequestWithEight, learning);

        languageService.addLanguages(userId, newLangRequestWithEight, learning);

        verify(languageMapper, times(1)).countLanguages(userId, learning);
        verify(languageMapper, times(1)).getLanguages(userId);
        verify(languageMapper, times(1)).insertLanguages(userId, newLangRequestWithEight, learning);
    }

    @Test
    @DisplayName("중복, 레벨설정에 대한 문제 없이, DB에 등록되 있는 언어를 포함해, 해당 언어 status 의 총 limit(8) 수를 넘지 않는 언어를 추가할 경우 테스트에 성공합니다.")
    public void addLearningLanguageSuccessUnderLimitWithDBData() {
        when(languageMapper.countLanguages(userId, learning)).thenReturn(existingLangData.size());
        when(languageMapper.getLanguages(userId)).thenReturn(existingLangData);
        doNothing().when(languageMapper).insertLanguages(userId, newLangRequestWithFour, learning);

        languageService.addLanguages(userId, newLangRequestWithFour, learning);

        verify(languageMapper, times(1)).countLanguages(userId, learning);
        verify(languageMapper, times(1)).getLanguages(userId);
        verify(languageMapper, times(1)).insertLanguages(userId, newLangRequestWithFour, learning);
    }

    @Test
    @DisplayName("DB에 저장된 정보가 없어도 추가할 언어의 수가 해당 언어 status 의 limit(8) 를 초과할 경우 LanguageLimitExceededException 가 발생하며 테스트에 실패합니다.")
    public void addLearningLanguageFailOverLimit() {
        when(languageMapper.countLanguages(userId, learning)).thenReturn(0);
        newLangRequestWithEight.add(new LanguageData(TestLangId.RUSSIAN, LanguageLevel.ADVANCED));

        assertThrows(LanguageLimitExceededException.class, () -> {
            languageService.addLanguages(userId, newLangRequestWithEight, learning);
        });

        verify(languageMapper, times(1)).countLanguages(userId, learning);
    }

    @Test
    @DisplayName("DB에 저장된 정보를 포함해 해당 언어 status 의 limit(8) 을 초과하는 숫자의 언어를 추가할 경우 LanguageLimitExceededException 가 발생하며 테스트에 실패합니다.")
    public void addLearningLanguageFailOverLimitWithDBData() {
        when(languageMapper.countLanguages(userId, learning)).thenReturn(existingLangData.size());

        assertThrows(LanguageLimitExceededException.class, () -> {
            languageService.addLanguages(userId, newLangRequestWithEight, learning);
        });

        verify(languageMapper, times(1)).countLanguages(userId, learning);
    }

    @Test
    @DisplayName("LEARNING status 의 언어에 대해 NATIVE 로 레벨 설정이 되어 추가 요청이 들어오면 InvalidLanguageLevelException 이 발생하며 테스트에 실패합니다.")
    public void addLearningLanguageFailWithInvalidLanguageLevel() {
        when(languageMapper.countLanguages(userId, learning)).thenReturn(existingLangData.size());

        List<LanguageData> newLangWithInappropriateLevel = new ArrayList<>();
        newLangWithInappropriateLevel.add(new LanguageData(TestLangId.English, LanguageLevel.NATIVE));

        assertThrows(InvalidLanguageLevelException.class, () -> {
            languageService.addLanguages(userId, newLangWithInappropriateLevel, learning);
        });

        verify(languageMapper, times(1)).countLanguages(userId, learning);
    }

    @Test
    @DisplayName("새롭게 추가 요청을 보낸 언어들에 중복이 있는 경우 DuplicateLanguageException 이 발생하며 테스트에 실패합니다.")
    public void addLearningLanguageFailWithDuplicateInNewLangData() {
        when(languageMapper.getLanguages(userId)).thenReturn(existingLangData);

        assertThrows(DuplicateLanguageException.class, () -> {
            languageService.addLanguages(userId, duplicateInNewLangRequest, learning);
        });

        verify(languageMapper, times(1)).getLanguages(userId);
    }

    @Test
    @DisplayName("새롭게 추가 요청을 보낸 언어 중 기존에 추가된 언어와 중복이 있는 경우 DuplicateLanguageException 이 발생하며 테스트에 실패합니다.")
    public void addLearningLanguageFailWithDuplicateExistingLangData() {
        when(languageMapper.getLanguages(userId)).thenReturn(existingLangData);

        assertThrows(DuplicateLanguageException.class, () -> {
            languageService.addLanguages(userId, newLangDuplicateWithExisting, learning);
        });

        verify(languageMapper, times(1)).getLanguages(userId);
    }

    /*
        LanguageStatus.CAN_SPEAK 에 대한 테스트를
        담고있는 부분입니다.
     */

    @Test
    @DisplayName("중복, 레벨설정에 대한 문제 없이, 해당 언어 status 의 limit(4) 수를 넘지 않는 언어를 새롭게 추가할 경우 테스트에 성공합니다. - DB에 기존 데이터 없음")
    public void addCanSpeakLanguageSuccessUnderLimit() {
        when(languageMapper.countLanguages(userId, canSpeak)).thenReturn(0);
        when(languageMapper.getLanguages(userId)).thenReturn(new ArrayList<>(0));
        doNothing().when(languageMapper).insertLanguages(userId, newLangRequestWithFour, canSpeak);

        languageService.addLanguages(userId, newLangRequestWithFour, canSpeak);

        verify(languageMapper, times(1)).countLanguages(userId, canSpeak);
        verify(languageMapper, times(1)).getLanguages(userId);
        verify(languageMapper, times(1)).insertLanguages(userId, newLangRequestWithFour, canSpeak);
    }

    @Test
    @DisplayName("중복, 레벨설정에 대한 문제 없이, DB에 등록되 있는 언어를 포함해, 해당 언어 status 의 총 limit(4) 수를 넘지 않는 언어를 추가할 경우 테스트에 성공합니다.")
    public void addCanSpeakLanguageSuccessUnderLimitWithDBData() {
        when(languageMapper.countLanguages(userId, canSpeak)).thenReturn(existingLangData.size());
        when(languageMapper.getLanguages(userId)).thenReturn(existingLangData);
        doNothing().when(languageMapper).insertLanguages(userId, newLangRequestWithTwo, canSpeak);

        languageService.addLanguages(userId, newLangRequestWithTwo, canSpeak);

        verify(languageMapper, times(1)).countLanguages(userId, canSpeak);
        verify(languageMapper, times(1)).getLanguages(userId);
        verify(languageMapper, times(1)).insertLanguages(userId, newLangRequestWithTwo, canSpeak);
    }

    @Test
    @DisplayName("DB에 저장된 정보가 없어도 추가할 언어의 수가 해당 언어 status 의 limit(4) 를 초과할 경우 LanguageLimitExceededException 가 발생하며 테스트에 실패합니다.")
    public void addCanSpeakLanguageFailOverLimit() {
        when(languageMapper.countLanguages(userId, canSpeak)).thenReturn(0);

        // 이미 4개를 담고 있는 새로운 언어 데이터 리스트에 예외 처리를 위해 1개 더 추가
        newLangRequestWithFour.add(new LanguageData(TestLangId.JAPANESE, LanguageLevel.ADVANCED));

        assertThrows(LanguageLimitExceededException.class, () -> {
            languageService.addLanguages(userId, newLangRequestWithFour, canSpeak);
        });

        verify(languageMapper, times(1)).countLanguages(userId, canSpeak);
    }

    @Test
    @DisplayName("DB에 저장된 정보를 포함해 해당 언어 status 의 limit(4) 을 초과하는 숫자의 언어를 추가할 경우 LanguageLimitExceededException 가 발생하며 테스트에 실패합니다.")
    public void addCanSpeakLanguageFailOverLimitWithDBData() {
        when(languageMapper.countLanguages(userId, canSpeak)).thenReturn(existingLangData.size());

        assertThrows(LanguageLimitExceededException.class, () -> {
            languageService.addLanguages(userId, newLangRequestWithFour, canSpeak);
        });

        verify(languageMapper, times(1)).countLanguages(userId, canSpeak);
    }

    @Test
    @DisplayName("CAN_SPEAK status 의 언어에 대해 NATIVE 로 레벨 설정이 되어 추가 요청이 들어오면 InvalidLanguageLevelException 이 발생하며 테스트에 실패합니다.")
    public void addCanSpeakLanguageFailWithInvalidLanguageLevel() {
        when(languageMapper.countLanguages(userId, canSpeak)).thenReturn(existingLangData.size());

        List<LanguageData> newLangWithInappropriateLevel = new ArrayList<>();
        newLangWithInappropriateLevel.add(new LanguageData(TestLangId.English, LanguageLevel.NATIVE));
        newLangWithInappropriateLevel.add(new LanguageData(TestLangId.PORTUGUESE, LanguageLevel.UPPER_INTERMEDIATE));

        assertThrows(InvalidLanguageLevelException.class, () -> {
            languageService.addLanguages(userId, newLangWithInappropriateLevel, canSpeak);
        });

        verify(languageMapper, times(1)).countLanguages(userId, canSpeak);
    }

    @Test
    @DisplayName("새롭게 추가 요청을 보낸 언어들에 중복이 있는 경우 DuplicateLanguageException 이 발생하며 테스트에 실패합니다.")
    public void addCanSpeakLanguageFailWithDuplicateInNewLangData() {
        when(languageMapper.countLanguages(userId, canSpeak)).thenReturn(existingLangData.size());
        when(languageMapper.getLanguages(userId)).thenReturn(existingLangData);

        assertThrows(DuplicateLanguageException.class, () -> {
            languageService.addLanguages(userId, duplicateInNewLangRequest, canSpeak);
        });

        verify(languageMapper, times(1)).countLanguages(userId, canSpeak);
        verify(languageMapper, times(1)).getLanguages(userId);
    }

    @Test
    @DisplayName("새롭게 추가 요청을 보낸 언어 중 기존에 추가된 언어와 중복이 있는 경우 DuplicateLanguageException 이 발생하며 테스트에 실패합니다.")
    public void addCanSpeakLanguageFailWithDuplicateExistingLangData() {
        when(languageMapper.countLanguages(userId, canSpeak)).thenReturn(existingLangData.size());
        when(languageMapper.getLanguages(userId)).thenReturn(existingLangData);

        assertThrows(DuplicateLanguageException.class, () -> {
            languageService.addLanguages(userId, newLangDuplicateWithExisting, canSpeak);
        });

        verify(languageMapper, times(1)).countLanguages(userId, canSpeak);
        verify(languageMapper, times(1)).getLanguages(userId);
    }

    /*
        LanguageStatus.NATIVE 에 대한 테스트를
        담고있는 부분입니다.
     */

    @Test
    @DisplayName("중복, 레벨설정에 대한 문제 없이, 해당 언어 status 의 limit(4) 수를 넘지 않는 언어를 추가할 경우 테스트에 성공합니다. - NATIVE/DB에 저장된 정보 또한 존재하지 않을 때")
    public void addNativeLanguageSuccessUnderLimit() {
        when(languageMapper.countLanguages(userId, nativeLang)).thenReturn(0);
        when(languageMapper.getLanguages(userId)).thenReturn(new ArrayList<>(0));
        doNothing().when(languageMapper).insertLanguages(userId, newNativeLangRequestWithFour, nativeLang);

        languageService.addLanguages(userId, newNativeLangRequestWithFour, nativeLang);

        verify(languageMapper, times(1)).countLanguages(userId, nativeLang);
        verify(languageMapper, times(1)).getLanguages(userId);
        verify(languageMapper, times(1)).insertLanguages(userId, newNativeLangRequestWithFour, nativeLang);
    }

    @Test
    @DisplayName("중복, 레벨설정에 대한 문제 없이, DB에 등록되 있는 언어를 포함해, 해당 언어 status 의 총 limit(4) 수를 넘지 않는 언어를 추가할 경우 테스트에 성공합니다.")
    public void addNativeLanguageSuccessUnderLimitWithDBData() {
        when(languageMapper.countLanguages(userId, nativeLang)).thenReturn(existingLangData.size());
        when(languageMapper.getLanguages(userId)).thenReturn(existingLangData);
        doNothing().when(languageMapper).insertLanguages(userId, newNativeLangRequestWithTwo, nativeLang);

        languageService.addLanguages(userId, newNativeLangRequestWithTwo, nativeLang);

        verify(languageMapper, times(1)).countLanguages(userId, nativeLang);
        verify(languageMapper, times(1)).getLanguages(userId);
        verify(languageMapper, times(1)).insertLanguages(userId, newNativeLangRequestWithTwo, nativeLang);
    }

    @Test
    @DisplayName("DB에 저장된 정보가 없어도 추가할 언어의 수가 해당 언어 status 의 limit(4) 를 초과할 경우 LanguageLimitExceededException 가 발생하며 테스트에 실패합니다.")
    public void addNativeLanguageFailOverLimit() {
        when(languageMapper.countLanguages(userId, nativeLang)).thenReturn(0);

        // 이미 4개를 담고 있는 새로운 언어 데이터 리스트에 예외 처리를 위해 모국어를 1개 더 추가
        newNativeLangRequestWithFour.add(new LanguageData(TestLangId.JAPANESE, LanguageLevel.NATIVE));

        assertThrows(LanguageLimitExceededException.class, () -> {
            languageService.addLanguages(userId, newNativeLangRequestWithFour, nativeLang);
        });

        verify(languageMapper, times(1)).countLanguages(userId, nativeLang);
    }

    @Test
    @DisplayName("DB에 저장된 정보를 포함해 해당 언어 status 의 limit(4) 을 초과하는 숫자의 언어를 추가할 경우 LanguageLimitExceededException 가 발생하며 테스트에 실패합니다.")
    public void addNativeLanguageFailOverLimitWithDBData() {
        when(languageMapper.countLanguages(userId, nativeLang)).thenReturn(existingNativeLangData.size());

        assertThrows(LanguageLimitExceededException.class, () -> {
            languageService.addLanguages(userId, newNativeLangRequestWithFour, nativeLang);
        });

        verify(languageMapper, times(1)).countLanguages(userId, nativeLang);
    }

    @Test
    @DisplayName("NATIVE status 의 언어에 대해 NATIVE 가 아닌 다른 언어레벨 설정으로 추가 요청이 들어오면 InvalidLanguageLevelException 이 발생하며 테스트에 실패합니다.")
    public void addNativeLanguageFailWithInvalidLanguageLevel() {
        when(languageMapper.countLanguages(userId, nativeLang)).thenReturn(existingNativeLangData.size());

        List<LanguageData> newNativeLangWithInappropriateLevel = new ArrayList<>();
        newNativeLangWithInappropriateLevel.add(new LanguageData(TestLangId.English, LanguageLevel.BEGINNER));
        newNativeLangWithInappropriateLevel.add(new LanguageData(TestLangId.KOREAN, LanguageLevel.NATIVE));

        assertThrows(InvalidLanguageLevelException.class, () -> {
            languageService.addLanguages(userId, newNativeLangWithInappropriateLevel, nativeLang);
        });

        verify(languageMapper, times(1)).countLanguages(userId, nativeLang);
    }

    @Test
    @DisplayName("새롭게 추가 요청을 보낸 언어들에 중복이 있는 경우 DuplicateLanguageException 이 발생하며 테스트에 실패합니다.")
    public void addNativeLanguageFailWithDuplicateInNewLangData() {
        when(languageMapper.countLanguages(userId, nativeLang)).thenReturn(existingNativeLangData.size());
        when(languageMapper.getLanguages(userId)).thenReturn(existingNativeLangData);

        assertThrows(DuplicateLanguageException.class, () -> {
            languageService.addLanguages(userId, duplicateInNewNativeLangRequest, nativeLang);
        });

        verify(languageMapper, times(1)).countLanguages(userId, nativeLang);
        verify(languageMapper, times(1)).getLanguages(userId);
    }

    @Test
    @DisplayName("새롭게 추가 요청을 보낸 언어 중 기존에 추가된 언어와 중복이 있는 경우 DuplicateLanguageException 이 발생하며 테스트에 실패합니다.")
    public void addNativeLanguageFailWithDuplicateExistingLangData() {
        when(languageMapper.countLanguages(userId, nativeLang)).thenReturn(existingNativeLangData.size());
        when(languageMapper.getLanguages(userId)).thenReturn(existingNativeLangData);

        assertThrows(DuplicateLanguageException.class, () -> {
            languageService.addLanguages(userId, newNativeLangDuplicateWithExisting, nativeLang);
        });

        verify(languageMapper, times(1)).countLanguages(userId, nativeLang);
        verify(languageMapper, times(1)).getLanguages(userId);
    }
}
