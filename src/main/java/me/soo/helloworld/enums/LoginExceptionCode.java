package me.soo.helloworld.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LoginExceptionCode implements ExceptionCode {
    DUPLICATE_LOGIN_REQUEST(HttpStatus.UNAUTHORIZED, "해당 사용자는 이미 로그인 되어 있습니다.");

    private final HttpStatus status;

    private final String description;

}
