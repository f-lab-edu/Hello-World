package me.soo.helloworld.exception;

public class DuplicateLoginRequestException extends RuntimeException {

    public DuplicateLoginRequestException(String message) {
        super(message);
    }

    public DuplicateLoginRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
