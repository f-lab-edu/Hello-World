package me.soo.helloworld.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ExceptionResponse {

    private final String title;

    private final String detail;

}
