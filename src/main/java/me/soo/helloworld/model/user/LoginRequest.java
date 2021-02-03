package me.soo.helloworld.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "아이디를 입력하세요.")
    private final String userId;

    @NotBlank(message = "비밀번호를 입력하세요.")
    private final String password;
}
