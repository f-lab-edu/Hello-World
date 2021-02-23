package me.soo.helloworld.service.language;

import me.soo.helloworld.enumeration.Language;
import me.soo.helloworld.enumeration.LanguageLevel;
import me.soo.helloworld.enumeration.LanguageStatus;
import me.soo.helloworld.exception.DuplicateLanguageException;
import me.soo.helloworld.model.language.LanguageData;
import me.soo.helloworld.repository.LanguageRepository;
import me.soo.helloworld.service.LanguageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class LanguageServiceIntegrationTest {
    private final String userId = "Soo1045";

    @Autowired
    LanguageService languageService;

    @Autowired
    LanguageRepository languageRepository;

    ArrayList<LanguageData> newLearningLang;

    ArrayList<LanguageData> newCanSpeakLang;

    ArrayList<LanguageData> newNativeLang;

    @BeforeEach
    public void setUp() {
        newLearningLang = new ArrayList<>();
        newLearningLang.add(new LanguageData(Language.ENGLISH, LanguageLevel.BEGINNER));
        newLearningLang.add(new LanguageData(Language.KOREAN, LanguageLevel.UPPER_INTERMEDIATE));

        newCanSpeakLang = new ArrayList<>();
        newCanSpeakLang.add(new LanguageData(Language.ENGLISH, LanguageLevel.ADVANCED));
        newCanSpeakLang.add(new LanguageData(Language.FRENCH, LanguageLevel.ADVANCED));

        newNativeLang = new ArrayList<>();
        newNativeLang.add(new LanguageData(Language.ENGLISH, LanguageLevel.NATIVE));
        newNativeLang.add(new LanguageData(Language.SPANISH, LanguageLevel.NATIVE));
    }
    /*
        하나의 언어는 추가하려는 status 를 다르게 지정해도 중복으로 추가할 수 없습니다.
        같은 언어를 중복해서 추가 시 예외가 발생합니다.
        위의 로직을 제대로 테스트 해보기 위한 통합 테스트 (Service - Repository - DB)
     */
    @Test
    @DisplayName("추가하려는 언어가 CAN_SPEAK status 에 없더라도 LEARNING status 에 이미 존재하면 DuplicateLanguageException 이 발생하며 테스트가 실패합니다.")
    public void addLanguageFailWithDuplicateLearningAndCanSpeak() {
        languageService.addLanguages(userId, newLearningLang, LanguageStatus.LEARNING);

        assertThrows(DuplicateLanguageException.class, () -> {
           languageService.addLanguages(userId, newCanSpeakLang, LanguageStatus.CAN_SPEAK);
        });
    }

    @Test
    @DisplayName("추가하려는 언어가 NATIVE status 에 없더라도 LEARNING status 에 이미 존재하면 DuplicateLanguageException 이 발생하며 테스트가 실패합니다.")
    public void addLanguageFailWithDuplicateLearningAndNATIVE() {
        languageService.addLanguages(userId, newLearningLang, LanguageStatus.LEARNING);

        assertThrows(DuplicateLanguageException.class, () -> {
            languageService.addLanguages(userId, newNativeLang, LanguageStatus.NATIVE);
        });
    }

    @Test
    @DisplayName("추가하려는 언어가 LEARNING status 에 없더라도 CAN_SPEAK status 에 이미 존재하면 DuplicateLanguageException 이 발생하며 테스트가 실패합니다.")
    public void addLanguageFailWithDuplicateCanSpeakAndLearning() {
        languageService.addLanguages(userId, newCanSpeakLang, LanguageStatus.CAN_SPEAK);

        assertThrows(DuplicateLanguageException.class, () -> {
            languageService.addLanguages(userId, newLearningLang, LanguageStatus.LEARNING);
        });
    }

    @Test
    @DisplayName("추가하려는 언어가 NATIVE status 에 없더라도 CAN_SPEAK status 에 이미 존재하면 DuplicateLanguageException 이 발생하며 테스트가 실패합니다.")
    public void addLanguageFailWithDuplicateCanSpeakAndNative() {
        languageService.addLanguages(userId, newCanSpeakLang, LanguageStatus.CAN_SPEAK);

        assertThrows(DuplicateLanguageException.class, () -> {
            languageService.addLanguages(userId, newNativeLang, LanguageStatus.NATIVE);
        });
    }

    @Test
    @DisplayName("추가하려는 언어가 LEARNING status 에 없더라도 NATIVE status 에 이미 존재하면 DuplicateLanguageException 이 발생하며 테스트가 실패합니다.")
    public void addLanguageFailWithDuplicateNativeAndLearning() {
        languageService.addLanguages(userId, newNativeLang, LanguageStatus.NATIVE);

        assertThrows(DuplicateLanguageException.class, () -> {
            languageService.addLanguages(userId, newLearningLang, LanguageStatus.LEARNING);
        });
    }

    @Test
    @DisplayName("추가하려는 언어가 CAN_SPEAK status 에 없더라도 NATIVE status 에 이미 존재하면 DuplicateLanguageException 이 발생하며 테스트가 실패합니다.")
    public void addLanguageFailWithDuplicateNativeAndCanSpeak() {
        languageService.addLanguages(userId, newNativeLang, LanguageStatus.NATIVE);

        assertThrows(DuplicateLanguageException.class, () -> {
            languageService.addLanguages(userId, newCanSpeakLang, LanguageStatus.CAN_SPEAK);
        });
    }

    @Test
    @DisplayName("추가하려는 언어가 이미 한 status 에 등록되 있으면 다른 status 로 등록 시 DuplicateLanguageException 이 발생하며 테스트가 실패합니다.")
    public void addLanguageFailWithDuplicateInOtherStatus() {
        languageService.addLanguages(userId, newLearningLang, LanguageStatus.LEARNING);

        assertThrows(DuplicateLanguageException.class, () -> {
            languageService.addLanguages(userId, newCanSpeakLang, LanguageStatus.CAN_SPEAK);
        });

        assertThrows(DuplicateLanguageException.class, () -> {
            languageService.addLanguages(userId, newLearningLang, LanguageStatus.LEARNING);
        });
    }


}
