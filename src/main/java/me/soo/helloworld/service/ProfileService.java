package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.enumeration.LanguageLevel;
import me.soo.helloworld.enumeration.LanguageStatus;
import me.soo.helloworld.exception.InvalidRequestException;
import me.soo.helloworld.exception.language.InvalidLanguageLevelException;
import me.soo.helloworld.mapper.ProfileMapper;
import me.soo.helloworld.model.condition.SearchConditions;
import me.soo.helloworld.model.condition.SearchConditionsRequest;
import me.soo.helloworld.model.language.Language;
import me.soo.helloworld.model.language.LanguageDataForProfile;
import me.soo.helloworld.model.user.UserProfile;
import me.soo.helloworld.model.user.UserDataOnProfile;
import me.soo.helloworld.model.recommendation.RecommendationForProfile;
import me.soo.helloworld.model.user.UserProfiles;
import me.soo.helloworld.util.Pagination;
import me.soo.helloworld.util.validator.LanguageLevelValidator;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static me.soo.helloworld.util.CacheNames.*;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileMapper profileMapper;

    private final FetchNameService fetchNameService;

    private final RecommendationService recommendationService;

    @Transactional(readOnly = true)
    @Cacheable(key = "#targetId", value = USER_PROFILE, cacheManager = REDIS_CACHE_MANAGER)
    public UserProfile getUserProfile(String targetId, String userId) {
        UserDataOnProfile profile = profileMapper.getUserProfileData(targetId, userId)
                .orElseThrow(() -> new InvalidRequestException("존재하지 않거나 정보가 올바르게 등록되지 않은 사용자의 경우 프로필 조회가 불가능합니다."));

        List<RecommendationForProfile> recommendations = recommendationService.getRecommendationsForProfile(targetId);

        return UserProfile.create(profile, matchCountry(profile.getOriginCountryId()), matchCountry(profile.getLivingCountryId()),
                matchTown(profile.getLivingTownId()), matchLanguages(profile.getLanguages()), recommendations);
    }

    @Transactional(readOnly = true)
    @Caching(cacheable = {
            @Cacheable(key = MAIN_PAGE_KEY, value = MAIN_PAGE_VALUE, condition = "#pagination.cursor == null", cacheManager = REDIS_CACHE_MANAGER),
            @Cacheable(key = "#pagination.cursor", value = USER_PROFILES, condition = "#pagination.cursor != null", cacheManager = REDIS_CACHE_MANAGER)
    })
    public List<UserProfiles> getUserProfiles(String userId, Pagination pagination) {
        return profileMapper.getUserProfiles(userId, pagination);
    }

    @Transactional(readOnly = true)
    public List<UserProfiles> searchUserProfiles(SearchConditionsRequest conditionsRequest, String userId, Pagination pagination) {
        List<LanguageLevel> learningLangLevels = conditionsRequest.getLearningLanguageLevel();
        LanguageLevelValidator.validateLevel(learningLangLevels, LanguageStatus.LEARNING);

        SearchConditions conditions = SearchConditions.create(conditionsRequest, userId, pagination);
        return profileMapper.searchUserProfiles(conditions);
    }

    private String matchCountry(Integer id) {
        return fetchNameService.loadAllCountriesMap().get(id);
    }

    private String matchTown(Integer id) {
        return fetchNameService.loadAllTownsMap().get(id);
    }

    private List<Language> matchLanguages(List<LanguageDataForProfile> languagesData) {
        Map<Integer, String> allLanguagesMap = fetchNameService.loadAllLanguagesMap();

        return languagesData.stream().map(language ->
                Language.builder()
                        .name(allLanguagesMap.get(language.getId()))
                        .level(language.getLevel())
                        .status(language.getStatus())
                        .build())
                .collect(Collectors.toList());
    }
}
