package me.soo.helloworld.exception;

import lombok.Getter;
import me.soo.helloworld.enums.FileExceptionCode;

@Getter
public class FileException extends RuntimeException{

    private FileExceptionCode fileExceptionCode;

    public FileException(FileExceptionCode userExceptionCode) {
        this.fileExceptionCode = userExceptionCode;
    }

}
