package me.soo.helloworld.exception;

public class LanguageLimitExceededException extends RuntimeException {

    public LanguageLimitExceededException(String message) {
        super(message);
    }

    public LanguageLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
