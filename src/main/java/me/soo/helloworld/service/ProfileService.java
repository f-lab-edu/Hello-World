package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.exception.InvalidRequestException;
import me.soo.helloworld.mapper.ProfileMapper;
import me.soo.helloworld.model.language.Language;
import me.soo.helloworld.model.language.LanguageDataForProfile;
import me.soo.helloworld.model.user.UserProfile;
import me.soo.helloworld.model.user.UserDataOnProfile;
import me.soo.helloworld.model.recommendation.RecommendationDataForProfile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileMapper profileMapper;

    private final FetchNameService fetchNameService;

    private final RecommendationService recommendationService;

    @Transactional(readOnly = true)
    public UserProfile getUserProfile(String userId) {
        UserDataOnProfile profile = profileMapper.getUserProfileData(userId)
                .orElseThrow(() -> new InvalidRequestException("존재하지 않거나 정보가 올바르게 등록되지 않은 사용자의 경우 프로필 조회가 불가능합니다."));

        List<RecommendationDataForProfile> recommendations = recommendationService.getRecommendationsForProfile(userId);

        return UserProfile.create(profile, matchCountry(profile.getOriginCountryId()), matchCountry(profile.getLivingCountryId()),
                matchTown(profile.getLivingTownId()), matchLanguages(profile.getLanguages()), recommendations);
    }

    private String matchCountry(Integer id) {
        return fetchNameService.loadAllCountriesMap().get(id);
    }

    private String matchTown(Integer id) {
        return fetchNameService.loadAllTownsMap().get(id);
    }

    private List<Language> matchLanguages(List<LanguageDataForProfile> languagesData) {
        Map<Integer, String> allLanguagesMap = fetchNameService.loadAllLanguagesMap();
        List<Language> languages = new ArrayList<>();

        languagesData.forEach(language ->
                languages.add(Language.builder()
                        .name(allLanguagesMap.get(language.getId()))
                        .level(language.getLevel())
                        .status(language.getStatus())
                        .build()));

        return languages;
    }
}
