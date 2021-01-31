package me.soo.helloworld.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

import javax.validation.constraints.NotBlank;

/**
 * 로그인을 위해 유저의 로그인 정보를 확인하기 위함
 *
 * 좀더 명확한 용도를 나타내기 위해 클래스 이름변경
 * : UserLoginInfo -> UserIdAndPassword
 */
@Getter
@AllArgsConstructor
public class UserIdAndPassword {

    @NotBlank(message = "아이디를 입력하세요.")
    private final String userId;

    @NotBlank(message = "비밀번호를 입력하세요.")
    private final String password;
}
