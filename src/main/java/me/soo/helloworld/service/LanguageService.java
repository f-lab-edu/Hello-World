package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.enumeration.Language;
import me.soo.helloworld.enumeration.LanguageLevel;
import me.soo.helloworld.enumeration.LanguageStatus;
import me.soo.helloworld.exception.DuplicateLanguageException;
import me.soo.helloworld.exception.InvalidLanguageLevelException;
import me.soo.helloworld.exception.LanguageLimitExceededException;
import me.soo.helloworld.model.language.LanguageData;
import me.soo.helloworld.repository.LanguageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LanguageService {

    private final LanguageRepository languageRepository;

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
    public void addLanguages(String userId, List<LanguageData> newLangData, LanguageStatus status) {
        int dbLangCounts = languageRepository.countLanguages(userId, status);

        if (dbLangCounts + newLangData.size() > status.getAddLimit()) {
            throw new LanguageLimitExceededException("해당 status 로 추가 가능한 언어의 개수를 초과하였습니다.");
        }

        checkProperLevel(newLangData, status);

        List<LanguageData> existingLangData = getLanguages(userId);
        checkDuplicateLanguage(existingLangData, newLangData);

        languageRepository.insertLanguages(userId, newLangData, status);
    }

    public List<LanguageData> getLanguages(String userId) {
        return languageRepository.getLanguages(userId);
    }

    /*
        중복된 언어 추가 요청이 들어왔는지 확인하는 메소드

        1. 새로 요청받은 언어 목록 중에 중복되는 언어가 있는 경우 예외 발생
        2. DB 내에 추가된 언어 목록과 비교해서 중복 요청이 들어온 경우 예외 발생
     */
    private void checkDuplicateLanguage(List<LanguageData> existingLangData, List<LanguageData> newLangData) {
        List<Language> newLang = extractLanguageOnly(newLangData);

        // 새로 요청 받은 언어목록을 확인
        if (newLang.size() != newLang.stream().distinct().count()) {
            throw new DuplicateLanguageException("새로 요청 받은 언어들 중에 중복되는 언어가 존재합니다. 중복 선택은 불가능 합니다.");
        }

        List<Language> existingLang = extractLanguageOnly(existingLangData);

        // 기존 언어목록과 새로 요청받은 언어목록을 비교
        if (newLang.stream().anyMatch(existingLang::contains)) {
            throw new DuplicateLanguageException("이미 추가되어 있는 언어는 다시 추가할 수 없습니다.");
        }
    }

    private List<Language> extractLanguageOnly(List<LanguageData> langList) {
        return langList.stream()
                .map(language -> langList.get(langList.indexOf(language)).getName())
                .collect(Collectors.toList());
    }


    /*
        추가 할 Status 맞게 언어 레벨을 설정했는지 확인하는 메소드

        1. Status 가 Native 인 경우
        - 추가하는 언어 모두 레벨이 NATIVE 로 이루어져 있어야 합니다.
        2. Status 가 Native 가 아닌 경우 (CAN_SPEAK or LEARNING)
        - 어떤 언어도 레벨이 NATIVE 가 되어서는 안됩니다.
     */
    private void checkProperLevel(List<LanguageData> newLangData, LanguageStatus status) {
        boolean isProperLevel;

        switch (status) {
            case NATIVE:
                isProperLevel = newLangData.stream()
                                            .allMatch(level -> newLangData.get(newLangData.indexOf(level))
                                            .getLevel().equals(LanguageLevel.NATIVE));
                break;
            case CAN_SPEAK:
            case LEARNING:
                isProperLevel = newLangData.stream()
                                        .noneMatch(level -> newLangData.get(newLangData.indexOf(level))
                                        .getLevel().equals(LanguageLevel.NATIVE));
                break;
            default:
                throw new InvalidLanguageLevelException("해당 언어 status 는 존재하지 않습니다. 모국어(NATIVE), 구사 가능언어(CAN_SPEAK)," +
                        " 학습 중인 언어(LEARNING) 중 한 가지만 선택해주세요.");
        }

        if (!isProperLevel) {
            throw new InvalidLanguageLevelException("추가하실 언어에 대한 레벨을 잘못 입력하셨습니다. 모국어 추가는 NATIVE 레벨로만 설정이 가능하며," +
                    " 모국어가 아닌 언어를 추가시에는 NATIVE 레벨로 설정이 불가능합니다.");
        }
    }


}
