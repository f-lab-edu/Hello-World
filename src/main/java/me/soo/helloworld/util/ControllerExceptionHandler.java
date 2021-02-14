package me.soo.helloworld.util;

import com.sun.jdi.request.DuplicateRequestException;
import lombok.extern.slf4j.Slf4j;
import me.soo.helloworld.exception.FileNotDeletedException;
import me.soo.helloworld.exception.FileNotUploadedException;
import me.soo.helloworld.exception.InvalidUserInfoException;
import me.soo.helloworld.model.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({
            FileNotDeletedException.class,
            FileNotUploadedException.class
    })
    public ResponseEntity<ExceptionResponse> fileException(final RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        ExceptionResponse response = new ExceptionResponse("파일에 대한 요청이 실패하였습니다.", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({
            InvalidUserInfoException.class,
    })
    public ResponseEntity<ExceptionResponse> userInvalidException(final InvalidUserInfoException ex) {
        log.error(ex.getMessage(), ex);
        ExceptionResponse response = new ExceptionResponse("사용자 정보가 일치하지 않습니다.", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateRequestException.class)
    public ResponseEntity<ExceptionResponse> loginFailedException(final RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        ExceptionResponse response = new ExceptionResponse("로그인에 실패하였습니다.", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

}
