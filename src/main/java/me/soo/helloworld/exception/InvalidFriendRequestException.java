package me.soo.helloworld.exception;

public class InvalidFriendRequestException extends RuntimeException {

    public InvalidFriendRequestException(String message) {
        super(message);
    }

    public InvalidFriendRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
