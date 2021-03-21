package me.soo.helloworld.util.handler;

import lombok.extern.slf4j.Slf4j;
import me.soo.helloworld.exception.*;
import me.soo.helloworld.exception.file.FileNotDeletedException;
import me.soo.helloworld.exception.file.FileNotUploadedException;
import me.soo.helloworld.exception.language.DuplicateLanguageException;
import me.soo.helloworld.exception.language.InvalidLanguageLevelException;
import me.soo.helloworld.exception.language.LanguageLimitExceededException;
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

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ExceptionResponse> invalidRequestException(final InvalidRequestException ex) {
        log.error(ex.getMessage(), ex);
        ExceptionResponse response = new ExceptionResponse("유효하지 않는 요청입니다.", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ExceptionResponse> unauthorizedAccessException(final UnauthorizedAccessException ex) {
        log.error(ex.getMessage(), ex);
        ExceptionResponse response = new ExceptionResponse("인증이 필요한 접근입니다.", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DuplicateRequestException.class)
    public ResponseEntity<ExceptionResponse> duplicateRequestException(final DuplicateRequestException ex) {
        log.error(ex.getMessage(), ex);
        ExceptionResponse response = new ExceptionResponse("중복된 요청입니다.", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchAlarmException.class)
    public ResponseEntity<ExceptionResponse> noSuchAlarmException (final NoSuchAlarmException ex) {
        log.error(ex.getMessage(), ex);
        ExceptionResponse response = new ExceptionResponse("존재하지 않는 리소스입니다.", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
