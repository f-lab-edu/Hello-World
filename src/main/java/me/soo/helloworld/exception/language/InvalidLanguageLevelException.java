package me.soo.helloworld.exception.language;

public class InvalidLanguageLevelException extends RuntimeException {

    public InvalidLanguageLevelException(String message) {
        super(message);
    }

    public InvalidLanguageLevelException(String message, Throwable cause) {
        super(message, cause);
    }
}
