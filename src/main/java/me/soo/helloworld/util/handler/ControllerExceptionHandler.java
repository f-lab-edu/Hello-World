package me.soo.helloworld.util.handler;

import com.sun.jdi.request.DuplicateRequestException;
import lombok.extern.slf4j.Slf4j;
import me.soo.helloworld.exception.*;
import me.soo.helloworld.model.exception.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
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

    @ExceptionHandler(InvalidUserInfoException.class)
    public ResponseEntity<ExceptionResponse> userInvalidException(final InvalidUserInfoException ex) {
        log.error(ex.getMessage(), ex);
        ExceptionResponse response = new ExceptionResponse("사용자 정보가 일치하지 않습니다.", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateRequestException.class)
    public ResponseEntity<ExceptionResponse> loginFailedException(final DuplicateRequestException ex) {
        log.error(ex.getMessage(), ex);
        ExceptionResponse response = new ExceptionResponse("로그인에 실패하였습니다.", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MailNotSentException.class)
    public ResponseEntity<ExceptionResponse> sendMailFailedException(final MailException ex) {
        log.error(ex.getMessage(), ex);
        ExceptionResponse response = new ExceptionResponse("이메일 발송에 실패하였습니다.", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({
            LanguageLimitExceededException.class,
            DuplicateLanguageException.class,
            InvalidLanguageLevelException.class
    })
    public ResponseEntity<ExceptionResponse> checkLanguageException(final RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        ExceptionResponse response = new ExceptionResponse("언어 정보 추가/변경/삭제에 실패하였습니다.", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            InvalidFriendRequestException.class,
            DuplicateFriendRequestException.class
    })
    public ResponseEntity<ExceptionResponse> friendRequestException(final RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        ExceptionResponse response = new ExceptionResponse("친구 추가 요청을 보내는데 실패하였습니다.", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
