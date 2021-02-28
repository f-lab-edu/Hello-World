package me.soo.helloworld.mapper;

import me.soo.helloworld.enumeration.LanguageStatus;
import me.soo.helloworld.model.language.LanguageData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LanguageMapper {

    public void insertLanguages(@Param("userId") String userId,
                                @Param("newLangData") List<LanguageData> newLangData,
                                LanguageStatus status);

    public int countLanguages(@Param("userId") String userId, @Param("status") LanguageStatus status);

    public List<LanguageData> getLanguages(String userId);

    public void updateLevel(@Param("userId") String userId,
                            @Param("langNewLevel") List<LanguageData> langNewLevel,
                            LanguageStatus status);

    public void deleteLanguages(@Param("userId") String userId,
                                @Param("languages") List<Integer> languages);
}
