package me.soo.helloworld.util;

import lombok.extern.slf4j.Slf4j;
import me.soo.helloworld.exception.file.FileException;
import me.soo.helloworld.exception.file.FileNotDeletedException;
import me.soo.helloworld.exception.file.FileNotUploadedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({
            FileNotUploadedException.class,
            FileNotDeletedException.class
    })
    public ResponseEntity<String> fileException(final FileException ex) {
        log.error("### FileException");
        log.error("### FileException has occurred during uploading or deleting files");
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
