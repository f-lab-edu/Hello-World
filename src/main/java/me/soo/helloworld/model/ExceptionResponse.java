package me.soo.helloworld.model;

import lombok.Getter;
import me.soo.helloworld.enums.ExceptionCode;
import org.springframework.http.HttpStatus;

@Getter
public class ExceptionResponse {

    private HttpStatus status;

    private String message;

    public ExceptionResponse(ExceptionCode exceptionCode) {
        this.status = exceptionCode.getStatus();
        this.message = exceptionCode.getDescription();
    }
}
