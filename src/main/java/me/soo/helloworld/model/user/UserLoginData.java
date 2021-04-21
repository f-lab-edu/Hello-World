package me.soo.helloworld.model.user;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class UserLoginData {

    private final String userId;

    private final String password;

    private final String token;
}
