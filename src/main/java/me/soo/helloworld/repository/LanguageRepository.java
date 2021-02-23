package me.soo.helloworld.repository;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.enumeration.LanguageStatus;
import me.soo.helloworld.mapper.LanguageMapper;
import me.soo.helloworld.model.language.LanguageData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LanguageRepository {

    private final LanguageMapper languageMapper;

    public void insertLanguages(String userId, List<LanguageData> newLangData, LanguageStatus status) {
        languageMapper.insertLanguages(userId, newLangData, status);
    }

    public int countLanguages(String userId, LanguageStatus status) {
        return languageMapper.countLanguages(userId, status);
    }

    public List<LanguageData> getLanguages(String userId) {
        return languageMapper.getLanguages(userId);
    }
}
