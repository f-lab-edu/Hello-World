package me.soo.helloworld.model.condition;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.soo.helloworld.annotation.AgeRange;
import me.soo.helloworld.enumeration.LanguageLevel;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@AgeRange
@RequiredArgsConstructor
public class SearchConditionsRequest {

    @Nullable
    @Pattern(message = "성별을 검색 조건으로 지정하기 위해서는 'M'(Male), 'F'(Female), 'O'(Other)의 세 조건 중 하나만 입력이 가능합니다.",
            regexp = "^[MFO]$")
    private final String gender;

    @Nullable
    private final Integer minAge;

    @Nullable
    private final Integer maxAge;

    @Nullable
    private final Integer originCountry;

    @Nullable
    private final Integer livingCountry;

    @Nullable
    private final Integer livingTown;

    @NotNull(message = "검색 시 상대방의 구사언어 정보는 필수로 입력하셔야 합니다.")
    private final Integer speakLanguage;

    @NotNull(message = "검색 시 상대방이 학습하고 있는 언어 정보는 필수로 입력하셔야 합니다.")
    private final Integer learningLanguage;

    @NotNull(message = "검색 시 상대방이 학습하고 있는 언어에 대한 언어 레벨정보는 필수로 입력하셔야 합니다.")
    private final List<LanguageLevel> learningLanguageLevel;
}
