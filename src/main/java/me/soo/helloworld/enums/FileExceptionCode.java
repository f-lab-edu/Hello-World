package me.soo.helloworld.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FileExceptionCode implements ExceptionCode {
    FILE_NOT_UPLOADED(HttpStatus.INTERNAL_SERVER_ERROR, "해당 파일에 대한 업로드가 실패하였습니다. 다시 시도해 주세요"),
    FILE_NOT_DELETED(HttpStatus.INTERNAL_SERVER_ERROR, "기존 파일을 제거하는데 실패하였습니다. 다시 시도해 주세요");

    private HttpStatus status;

    private String description;
}
