package me.soo.helloworld.exception;

public class FCMUninitializedException extends RuntimeException {

    public FCMUninitializedException(String message) {
        super(message);
    }

    public FCMUninitializedException(String message, Throwable cause) {
        super(message, cause);
    }
}
