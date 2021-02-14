package me.soo.helloworld.exception.file;

public class FileNotDeletedException extends FileException {

    public FileNotDeletedException(String message, Throwable cause) {
        super(message, cause);
    }
}
