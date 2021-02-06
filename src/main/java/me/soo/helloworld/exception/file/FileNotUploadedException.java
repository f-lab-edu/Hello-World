package me.soo.helloworld.exception.file;

public class FileNotUploadedException extends RuntimeException {

    public FileNotUploadedException(String message, Throwable cause) {
        super(message, cause);
    }
}
