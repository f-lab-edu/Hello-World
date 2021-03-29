package me.soo.helloworld.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserProfileList {

    private final int id;

    private final String userId;

    private final String aboutMe;

    private final String profileImageName;

    private final String profileImagePath;

    private int recommendationNums;
}
