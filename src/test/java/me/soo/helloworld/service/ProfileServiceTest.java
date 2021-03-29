package me.soo.helloworld.service;

import lombok.extern.slf4j.Slf4j;
import me.soo.helloworld.enumeration.LanguageLevel;
import me.soo.helloworld.enumeration.LanguageStatus;
import me.soo.helloworld.exception.InvalidRequestException;
import me.soo.helloworld.mapper.ProfileMapper;
import me.soo.helloworld.model.language.LanguageDataForProfile;
import me.soo.helloworld.model.recommendation.RecommendationForProfile;
import me.soo.helloworld.model.user.UserDataOnProfile;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static me.soo.helloworld.TestCountries.SOUTH_KOREA;
import static me.soo.helloworld.TestCountries.UNITED_KINGDOM;
import static me.soo.helloworld.TestLanguages.*;
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

    List<RecommendationForProfile> recommendations;

    @BeforeEach
    public void createRecommendations() {
        String firstFriend = "not Soo1";
        String secondFriend = "not Soo2";
        String content = "He is absolutely awesome to be friends with for language exchange.";

        recommendations = new ArrayList<>();
        recommendations.add(new RecommendationForProfile(firstFriend, content, Timestamp.valueOf(LocalDateTime.now())));
        recommendations.add(new RecommendationForProfile(secondFriend, content, Timestamp.valueOf(LocalDateTime.now())));
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
    }

    @Test
    @DisplayName("필수 정보를 모두 입력한 사용자의 경우, 추천 글이 존재하지 않아서 빈 리스트가 리턴되더라도 프로필 정보를 리턴하는데 성공합니다.")
    public void getUserProfileSuccessOnUserWithProperProfileDataWithoutAnyCachedMaps() {
        when(profileMapper.getUserProfileData(userId)).thenReturn(Optional.of(profileData));
        when(recommendationService.getRecommendationsForProfile(userId)).thenReturn(Collections.emptyList());

        profileService.getUserProfile(userId);

        verify(profileMapper, times(1)).getUserProfileData(userId);
        verify(recommendationService, times(1)).getRecommendationsForProfile(userId);
    }

    @Test
    @DisplayName("필수 정보를 모두 입력한 사용자의 경우, 추천 글이 존재 하면 해당 리스트를 포함해 프로필 정보를 리턴하는데 성공합니다.")
    public void getUserProfileSuccessOnUserWithProperProfileDataWithOnlyCachedCountriesMap() {
        when(profileMapper.getUserProfileData(userId)).thenReturn(Optional.of(profileData));
        when(recommendationService.getRecommendationsForProfile(userId)).thenReturn(recommendations);

        profileService.getUserProfile(userId);

        verify(profileMapper, times(1)).getUserProfileData(userId);
        verify(recommendationService, times(1)).getRecommendationsForProfile(userId);
    }
}
