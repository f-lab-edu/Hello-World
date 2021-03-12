package me.soo.helloworld.exception.language;

public class DuplicateLanguageException extends RuntimeException {

    public DuplicateLanguageException(String message) {
        super(message);
    }

    public DuplicateLanguageException(String message, Throwable cause) {
        super(message, cause);
    }
}
