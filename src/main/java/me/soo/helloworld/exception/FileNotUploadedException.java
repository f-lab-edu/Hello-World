package me.soo.helloworld.exception;

public class FileNotUploadedException extends RuntimeException {

    public FileNotUploadedException(String message) {
        super(message);
    }

    public FileNotUploadedException(String message, Throwable cause) {
        super(message, cause);
    }
}
