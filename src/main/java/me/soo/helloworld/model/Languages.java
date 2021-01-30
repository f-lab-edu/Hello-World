package me.soo.helloworld.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;


/**
 * 차후 유저의 언어 등록에 활용하기 위한 languages 클래스
 */
@Getter
@Builder
@AllArgsConstructor
public class Languages {

    @NotBlank(message = "모국어 정보를 입력해주세요.")
    private final String nativeLanguage;

    @NotEmpty(message = "배우는 언어의 정보를 입력해주세요.")
    private final String learningLanguage;

    @NotBlank(message = "배우는 언어의 현재 레벨 정보를 입력해주세요.")
    private final String learningLanguageLevel;
}
