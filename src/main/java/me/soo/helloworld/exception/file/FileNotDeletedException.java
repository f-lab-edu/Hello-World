package me.soo.helloworld.exception.file;

public class FileNotDeletedException extends RuntimeException {

    public FileNotDeletedException(String message) {
        super(message);
    }

    public FileNotDeletedException(String message, Throwable cause) {
        super(message, cause);
    }
}
