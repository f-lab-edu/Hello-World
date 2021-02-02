package me.soo.helloworld.exception;

public class IncorrectUserInfoException extends RuntimeException {

    public IncorrectUserInfoException(String message) {
        super(message);
    }
}
