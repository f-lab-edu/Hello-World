package me.soo.helloworld.enums;

import org.springframework.http.HttpStatus;

public interface ExceptionCode {

    public HttpStatus getStatus();

    public String getDescription();
}
