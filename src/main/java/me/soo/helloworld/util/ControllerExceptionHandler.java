package me.soo.helloworld.util;

import lombok.extern.slf4j.Slf4j;
import me.soo.helloworld.exception.FileException;
import me.soo.helloworld.exception.LoginException;
import me.soo.helloworld.exception.UserException;
import me.soo.helloworld.model.ExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(FileException.class)
    public ResponseEntity<ExceptionResponse> fileException(final FileException ex) {
        log.error("해당 파일에 대한 요청이 실패하였습니다.", ex);
        ExceptionResponse response = new ExceptionResponse(ex.getFileExceptionCode());
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<ExceptionResponse> loginException(final LoginException ex) {
        log.error("해당 유저 정보로 로그인하는데 실패하였습니다.", ex);
        ExceptionResponse response = new ExceptionResponse(ex.getLoginExceptionCode());
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ExceptionResponse> userException(final UserException ex) {
        log.error("해당 유저의 정보를 얻는데 실패하였습니다.", ex);
        ExceptionResponse response = new ExceptionResponse(ex.getUserExceptionCode());
        return new ResponseEntity<>(response, response.getStatus());
    }

}
