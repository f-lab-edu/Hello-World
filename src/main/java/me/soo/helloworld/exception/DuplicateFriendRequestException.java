package me.soo.helloworld.exception;

public class DuplicateFriendRequestException extends RuntimeException {

    public DuplicateFriendRequestException(String message) {
        super(message);
    }

    public DuplicateFriendRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
