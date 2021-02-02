package me.soo.helloworld.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Builder
@AllArgsConstructor
public class UserPasswordRequest {

    @NotBlank(message = "현재 비밀번호를 입력해주세요.")
    private final String currentPassword;

    @NotBlank(message = "새로운 비밀번호를 입력해주세요 (8 ~ 20자 이내, 영문 대/소문자, 숫자, 특수문자를 각각 1개 이상 포함)")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{8,20}")
    private final String newPassword;

    @NotBlank(message = "새로운 비밀번호를 다시 한 번 입력해주세요.")
    private final String checkNewPassword;

}
