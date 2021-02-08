package me.soo.helloworld.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserExceptionCode implements ExceptionCode {
    INVALID_USER_ID(HttpStatus.NOT_FOUND, "해당 아이디는 존재하지 않습니다. 아이디를 다시 확인해 주세요."),
    INVALID_USER_PASSWORD(HttpStatus.NOT_FOUND, "입력하신 비밀번호는 일치하지 않습니다. 비밀번호를 다시 확인해주세요.");

    private final HttpStatus status;

    private final String description;

}
