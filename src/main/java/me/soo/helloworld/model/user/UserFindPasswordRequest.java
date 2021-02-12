package me.soo.helloworld.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class UserFindPasswordRequest {

    @NotBlank(message = "아이디를 입력하세요.")
    private final String userId;

    @Email(message = "이메일을 형식에 맞게 입력해주세요")
    @NotBlank
    private final String email;
}
