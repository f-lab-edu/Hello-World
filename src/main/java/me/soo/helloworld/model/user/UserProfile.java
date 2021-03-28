package me.soo.helloworld.model.user;

import lombok.*;
import me.soo.helloworld.model.language.Language;
import me.soo.helloworld.model.recommendation.RecommendationDataForProfile;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserProfile {

    private String userId;

    private String gender;

    private int age;

    private String profileImageName;

    private String profileImagePath;

    private String aboutMe;

    private String originCountry;

    private String livingCountry;

    private String livingTown;

    private List<Language> languages;

    private List<RecommendationDataForProfile> recommendations;

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
