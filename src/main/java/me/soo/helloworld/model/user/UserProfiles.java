package me.soo.helloworld.model.user;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserProfiles {

    private int id;

    private String userId;

    private String aboutMe;

    private String profileImageName;

    private String profileImagePath;

    private int recommendationNums;

    public UserProfiles(int id, String userId, String aboutMe,
                        String profileImageName, String profileImagePath) {
        this.id = id;
        this.userId = userId;
        this.aboutMe = aboutMe;
        this.profileImageName = profileImageName;
        this.profileImagePath = profileImagePath;
    }
}
