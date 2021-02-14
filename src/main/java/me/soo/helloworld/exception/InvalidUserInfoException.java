package me.soo.helloworld.exception;

public class InvalidUserInfoException extends RuntimeException {

    public InvalidUserInfoException(String message) {
        super(message);
    }

    public InvalidUserInfoException(String message, Throwable cause) {
        super(message, cause);
    }
}
