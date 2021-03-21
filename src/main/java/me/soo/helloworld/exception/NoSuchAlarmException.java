package me.soo.helloworld.exception;

public class NoSuchAlarmException extends RuntimeException {

    public NoSuchAlarmException(String message) {
        super(message);
    }

    public NoSuchAlarmException(String message, Throwable cause) {
        super(message, cause);
    }
}
