package me.soo.helloworld.integrations;

import lombok.extern.slf4j.Slf4j;
import me.soo.helloworld.enumeration.LanguageLevel;
import me.soo.helloworld.enumeration.LanguageStatus;
import me.soo.helloworld.exception.language.LanguageLimitExceededException;
import me.soo.helloworld.model.language.LanguageData;
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
import java.util.List;
import java.util.stream.Collectors;

import static me.soo.helloworld.integrations.IntegrationTestLangId.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class ITDeleteLanguage {

    private final String user = "Soo1045";

    List<LanguageData> newLearningLangRequest;

    List<LanguageData> newCanSpeakLangRequest;

    List<LanguageData> newNativeLangRequest;

    List<Integer> langInDB;

    List<Integer> overLimitLang;

    @Autowired
    LanguageService languageService;

    @BeforeEach
    public void createNewLangRequests() {
        newLearningLangRequest = new ArrayList<>();
        newLearningLangRequest.add(new LanguageData(KOREAN, LanguageLevel.BEGINNER));
        newLearningLangRequest.add(new LanguageData(ENGLISH, LanguageLevel.BEGINNER));
        newLearningLangRequest.add(new LanguageData(FRENCH, LanguageLevel.BEGINNER));
        newLearningLangRequest.add(new LanguageData(SPANISH, LanguageLevel.BEGINNER));

        newCanSpeakLangRequest = new ArrayList<>();
        newCanSpeakLangRequest.add(new LanguageData(RUSSIAN, LanguageLevel.BEGINNER));
        newCanSpeakLangRequest.add(new LanguageData(GERMAN, LanguageLevel.BEGINNER));
        newCanSpeakLangRequest.add(new LanguageData(ITALIAN, LanguageLevel.BEGINNER));
        newCanSpeakLangRequest.add(new LanguageData(CHINESE_CANTONESE, LanguageLevel.BEGINNER));

        newNativeLangRequest = new ArrayList<>();
        newNativeLangRequest.add(new LanguageData(CHINESE_MANDARIN, LanguageLevel.NATIVE));
        newNativeLangRequest.add(new LanguageData(ARABIC, LanguageLevel.NATIVE));
        newNativeLangRequest.add(new LanguageData(DUTCH, LanguageLevel.NATIVE));
        newNativeLangRequest.add(new LanguageData(PORTUGUESE, LanguageLevel.NATIVE));
    }

    @BeforeEach
    public void createUserLangList() {
        langInDB = new ArrayList<>();
        langInDB.add(KOREAN);
        langInDB.add(ENGLISH);
        langInDB.add(FRENCH);
        langInDB.add(SPANISH);
        langInDB.add(RUSSIAN);
        langInDB.add(GERMAN);
        langInDB.add(ITALIAN);
        langInDB.add(CHINESE_CANTONESE);
        langInDB.add(CHINESE_MANDARIN);
        langInDB.add(ARABIC);
        langInDB.add(DUTCH);
        langInDB.add(PORTUGUESE);
    }

    @BeforeEach
    public void createOverLimitLangList() {
        overLimitLang = new ArrayList<>();
        overLimitLang.add(KOREAN);
        overLimitLang.add(ENGLISH);
        overLimitLang.add(FRENCH);
        overLimitLang.add(SPANISH);
        overLimitLang.add(RUSSIAN);
        overLimitLang.add(GERMAN);
        overLimitLang.add(ITALIAN);
        overLimitLang.add(CHINESE_CANTONESE);
        overLimitLang.add(CHINESE_MANDARIN);
        overLimitLang.add(ARABIC);
        overLimitLang.add(DUTCH);
        overLimitLang.add(PORTUGUESE);
        overLimitLang.add(HINDI);
        overLimitLang.add(JAPANESE);
        overLimitLang.add(BULGARIAN);
        overLimitLang.add(SWEDISH);
        overLimitLang.add(OTHER);
    }

    @Test
    @DisplayName("최대로 등록 가능한 언어 개수(16) 내로 삭제 요청이 들어올 시 등록된 언어 삭제에 성공합니다.")
    public void deleteLanguageSuccess() {
        languageService.addLanguages(user, newLearningLangRequest, LanguageStatus.LEARNING);
        languageService.addLanguages(user, newCanSpeakLangRequest, LanguageStatus.CAN_SPEAK);
        languageService.addLanguages(user, newNativeLangRequest, LanguageStatus.NATIVE);

        List<LanguageData> allLang = languageService.getLanguages(user);
        List<Integer> langIds = allLang.stream()
                                        .map(langId -> allLang.get(allLang.indexOf(langId)).getId())
                                        .collect(Collectors.toList());

        languageService.deleteLanguages(user, langIds);

        List<LanguageData> allLangAfterDelete = languageService.getLanguages(user);
        assertSame(allLangAfterDelete.size(), 0);
    }

    @Test
    @DisplayName("여러 사용가 같은 언어를 등록했다고 하더라도 해당 사용자가 요청한 언어에 대한 정보만 삭제합니다.")
    public void checkDeleteProperlyByUserId() {
        String anotherUser = "another";

        // user 의 언어 등록 (총 12개)
        languageService.addLanguages(user, newLearningLangRequest, LanguageStatus.LEARNING);
        languageService.addLanguages(user, newCanSpeakLangRequest, LanguageStatus.CAN_SPEAK);
        languageService.addLanguages(user, newNativeLangRequest, LanguageStatus.NATIVE);

        // anotherUser 의 언어 등록 (user 와 똑같은 언어, 총 12개)
        languageService.addLanguages(anotherUser, newLearningLangRequest, LanguageStatus.LEARNING);
        languageService.addLanguages(anotherUser, newCanSpeakLangRequest, LanguageStatus.CAN_SPEAK);
        languageService.addLanguages(anotherUser, newNativeLangRequest, LanguageStatus.NATIVE);

        languageService.deleteLanguages(user, langInDB);

        List<LanguageData> userLangInDB = languageService.getLanguages(user);
        List<LanguageData> anotherUserLangInDB = languageService.getLanguages(anotherUser);

        assertTrue(userLangInDB.isEmpty());
        assertSame(anotherUserLangInDB.size(), 12);

        List<Integer> anotherUserLang = anotherUserLangInDB.stream()
                .map(langId -> anotherUserLangInDB.get(anotherUserLangInDB.indexOf(langId)).getId())
                .collect(Collectors.toList());

        boolean isNotDeleted = langInDB.containsAll(anotherUserLang);
        assertTrue(isNotDeleted);
    }

    @Test
    @DisplayName("최대로 등록 가능한 언어 개수(16) 이상으로 언어 삭제 요청이 들어올 시 LanguageLimitExceededException 이 발생하며 삭제에 실패합니다.")
    public void deleteLanguagesFailOverTotalLanguageLimit() {
        assertThrows(LanguageLimitExceededException.class, () -> {
                languageService.deleteLanguages(user, overLimitLang);
        });
    }

}
