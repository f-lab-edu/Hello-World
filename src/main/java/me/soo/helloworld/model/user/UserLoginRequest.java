package me.soo.helloworld.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@AllArgsConstructor
public class UserLoginRequest {

    @NotBlank(message = "아이디를 입력하세요.")
    private final String userId;

    @NotBlank(message = "비밀번호를 입력하세요.")
    private final String password;

    @Nullable
    private final String token;
}
