package me.soo.helloworld.model.user;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class TestUserLoginData {

    private final String userId;

    private final String password;
}
