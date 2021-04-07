package me.soo.helloworld.model.user;

import lombok.Value;

@Value
public class UserProfileList {

    int id;

    String userId;

    String aboutMe;

    String profileImageName;

    String profileImagePath;

    int totalRecommendations;
}
