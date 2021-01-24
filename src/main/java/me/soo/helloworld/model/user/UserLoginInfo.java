package me.soo.helloworld.model.user;

import lombok.Getter;
import lombok.Value;

import javax.validation.constraints.NotBlank;

/**
 * 로그인을 위해 유저의 로그인 정보를 확인하기 위함
 * Value annotation? What is the purpose? - do some research and write it down here
 */
@Value
@Getter
public class UserLoginInfo {
    @NotBlank(message = "아이디를 입력하세요.")
    String userId;

    @NotBlank(message = "비밀번호를 입력하세요.")
    String password;
}
