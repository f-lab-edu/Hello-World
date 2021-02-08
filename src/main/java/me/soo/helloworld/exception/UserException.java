package me.soo.helloworld.exception;

import lombok.Getter;
import me.soo.helloworld.enums.UserExceptionCode;

@Getter
public class UserException extends RuntimeException {

    private UserExceptionCode userExceptionCode;

    public UserException(UserExceptionCode userExceptionCode) {
        this.userExceptionCode = userExceptionCode;
    }
}
