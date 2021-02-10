package me.soo.helloworld.util;

import com.sun.jdi.request.DuplicateRequestException;
import lombok.extern.slf4j.Slf4j;
import me.soo.helloworld.exception.FileNotDeletedException;
import me.soo.helloworld.exception.FileNotUploadedException;
import me.soo.helloworld.exception.InvalidUserException;
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
        log.error("해당 파일에 대한 요청이 실패하였습니다.", ex);
        ExceptionResponse response = new ExceptionResponse(500, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({
            InvalidUserException.class,
    })
    public ResponseEntity<ExceptionResponse> invalidUserException(final InvalidUserException ex) {
        log.error("해당 사용자 정보를 가지고 로그인하는데 실패하였습니다.", ex);
        ExceptionResponse response = new ExceptionResponse(404, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateRequestException.class)
    public ResponseEntity<ExceptionResponse> userException(final RuntimeException ex) {
        log.error("이미 로그인 되어 있는 사용자입니다.", ex);
        ExceptionResponse response = new ExceptionResponse(401, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

}
