package me.soo.helloworld.exception;

import lombok.Getter;
import me.soo.helloworld.enums.LoginExceptionCode;

@Getter
public class LoginException extends RuntimeException {

    private LoginExceptionCode loginExceptionCode;

    public LoginException(LoginExceptionCode loginExceptionCode) {
        this.loginExceptionCode = loginExceptionCode;
    }

}
