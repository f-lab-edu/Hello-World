package me.soo.helloworld.service;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import me.soo.helloworld.enumeration.LanguageLevel;
import me.soo.helloworld.enumeration.LanguageStatus;
import me.soo.helloworld.exception.InvalidRequestException;
import me.soo.helloworld.mapper.ProfileMapper;
import me.soo.helloworld.model.language.LanguageDataForProfile;
import me.soo.helloworld.model.recommendation.RecommendationDataForProfile;
import me.soo.helloworld.model.user.UserDataOnProfile;
import org.apache.ibatis.mapping.CacheBuilder;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Import;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static me.soo.helloworld.TestCountries.SOUTH_KOREA;
import static me.soo.helloworld.TestCountries.UNITED_KINGDOM;
import static me.soo.helloworld.TestLanguages.*;
import static me.soo.helloworld.TestTowns.*;
import static me.soo.helloworld.util.CacheNames.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {

    private final String userId = "Soo";

    @InjectMocks
    ProfileService profileService;

    @Mock
    ProfileMapper profileMapper;

    @Mock
    FetchNameService fetchNameService;

    @Mock
    RecommendationService recommendationService;

    UserDataOnProfile profileData;

    List<RecommendationDataForProfile> recommendations;

    Map<Integer, String> countriesMap;

    Map<Integer, String> townsMap;

    Map<Integer, String> languagesMap;

    @BeforeEach
    public void createCountriesMap() {
        countriesMap = new HashMap<>();
        countriesMap.put(SOUTH_KOREA, "South Korea");
        countriesMap.put(UNITED_KINGDOM, "United Kingdom");
    }

    @BeforeEach
    public void createTownsMap() {
        townsMap = new HashMap<>();
        townsMap.put(ABERDEEN, "Aberdeen");
        townsMap.put(BELFAST, "Belfast");
        townsMap.put(LIVERPOOL, "LiverPool");
    }

    @BeforeEach
    public void createLanguageMap() {
        languagesMap = new HashMap<>();
        languagesMap.put(KOREAN, "Korean");
        languagesMap.put(ENGLISH, "English");
        languagesMap.put(FRENCH, "French");
    }

    @BeforeEach
    public void createRecommendations() {
        String firstFriend = "not Soo1";
        String secondFriend = "not Soo2";
        String content = "He is absolutely awesome to be friends with for language exchange.";

        recommendations = new ArrayList<>();
        recommendations.add(new RecommendationDataForProfile(firstFriend, content));
        recommendations.add(new RecommendationDataForProfile(secondFriend, content));
    }

    @BeforeEach
    public void createUserProfileData() {
        LanguageDataForProfile nativeKorean = new LanguageDataForProfile(KOREAN, LanguageLevel.NATIVE, LanguageStatus.NATIVE);
        LanguageDataForProfile learningEnglish = new LanguageDataForProfile(ENGLISH, LanguageLevel.BEGINNER, LanguageStatus.LEARNING);

        List<LanguageDataForProfile> languagesData = new ArrayList<>();
        languagesData.add(nativeKorean);
        languagesData.add(learningEnglish);

        profileData = UserDataOnProfile.builder()
                .userId(userId)
                .gender("M")
                .age(20)
                .profileImageName("I am found.jpg")
                .profileImagePath("D:/Hello/World/Awesome")
                .aboutMe("Hello, I'm Soo. I'm excited to meet new people here.")
                .originCountryId(SOUTH_KOREA)
                .livingCountryId(UNITED_KINGDOM)
                .livingTownId(3)
                .languages(languagesData)
                .build();
    }

    @Test
    @DisplayName("존재하지 않거나 혹은 필수 정보가 등록되어 있지 않은 사용자의 프로필을 조회하는 경우 프로필 조회에 실패하며 InvalidRequestException 이 발생합니다.")
    public void getUserProfileFailOnNotExistingOrInvalidInfoUser() {
        String notProperUser = "I'm inappropriate";
        when(profileMapper.getUserProfileData(notProperUser)).thenReturn(Optional.empty());

        assertThrows(InvalidRequestException.class, () -> {
           profileService.getUserProfile(notProperUser);
        });

        verify(profileMapper, times(1)).getUserProfileData(notProperUser);
        verify(recommendationService, never()).getRecommendationsForProfile(notProperUser);
        verify(fetchNameService, never()).loadAllCountriesMap();
        verify(fetchNameService, never()).loadAllTownsMap();
        verify(fetchNameService, never()).loadAllLanguagesMap();
    }

    @Test
    @DisplayName("필수 정보를 모두 입력한 사용자의 경우, 캐시된 정보가 하나도 없는 경우 각 데이터를 DB 에서 로드 해 국가, 도시, 언어 이름을 매칭 시킨 후 프로필 정보를 리턴하는데 성공합니다.")
    public void getUserProfileSuccessOnUserWithProperProfileDataWithoutAnyCachedMaps() {
        when(profileMapper.getUserProfileData(userId)).thenReturn(Optional.of(profileData));
        when(fetchNameService.loadAllCountriesMap()).thenReturn(countriesMap);
        when(recommendationService.getRecommendationsForProfile(userId)).thenReturn(recommendations);

        profileService.getUserProfile(userId);

        verify(recommendationService, times(1)).getRecommendationsForProfile(userId);
        verify(fetchNameService, times(2)).loadAllCountriesMap();
        verify(fetchNameService, times(1)).loadAllTownsMap();
        verify(fetchNameService, times(1)).loadAllLanguagesMap();
    }

    @Test
    @DisplayName("필수 정보를 모두 입력한 사용자의 경우, 캐시된 정보가 하나도 없는 경우 각 데이터를 DB 에서 로드 해 국가, 도시, 언어 이름을 매칭 시킨 후 프로필 정보를 리턴하는데 성공합니다.")
    public void getUserProfileSuccessOnUserWithProperProfileDataWithOnlyCachedCountriesMap() {
        when(profileMapper.getUserProfileData(userId)).thenReturn(Optional.of(profileData));
        when(fetchNameService.loadAllCountriesMap()).thenReturn(countriesMap);
        when(recommendationService.getRecommendationsForProfile(userId)).thenReturn(recommendations);

        profileService.getUserProfile(userId);

        verify(recommendationService, times(1)).getRecommendationsForProfile(userId);
        verify(fetchNameService, times(2)).loadAllCountriesMap();
        verify(fetchNameService, times(1)).loadAllTownsMap();
        verify(fetchNameService, times(1)).loadAllLanguagesMap();
    }
}
