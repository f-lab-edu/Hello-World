package me.soo.helloworld.model.user;

import lombok.Builder;
import lombok.Getter;
import me.soo.helloworld.model.language.Language;
import me.soo.helloworld.model.recommendation.RecommendationDataForProfile;

import java.util.List;

@Getter
@Builder
public class UserProfile {

    private final String userId;

    private final String gender;

    private final int age;

    private final String profileImageName;

    private final String profileImagePath;

    private final String aboutMe;

    private final String originCountry;

    private final String livingCountry;

    private final String livingTown;

    private final List<Language> languages;

    private final List<RecommendationDataForProfile> recommendations;

    public static UserProfile create(UserDataOnProfile profileData, String originCountry, String livingCountry,
                                     String livingTown, List<Language> languages, List<RecommendationDataForProfile> recommendations) {

        return UserProfile.builder()
                        .userId(profileData.getUserId())
                        .gender(profileData.getGender())
                        .age(profileData.getAge())
                        .profileImageName(profileData.getProfileImageName())
                        .profileImagePath(profileData.getProfileImagePath())
                        .aboutMe(profileData.getAboutMe())
                        .originCountry(originCountry)
                        .livingCountry(livingCountry)
                        .livingTown(livingTown)
                        .languages(languages)
                        .recommendations(recommendations)
                        .build();
    }
}
