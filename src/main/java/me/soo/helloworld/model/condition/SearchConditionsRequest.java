package me.soo.helloworld.model.condition;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.soo.helloworld.annotation.AgeRange;
import me.soo.helloworld.enumeration.LanguageLevel;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Getter
@Builder
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

    @NotNull
    @Min(message = "상대방의 구사 언어 이름을 올바르게 입력해주세요.", value = 1)
    private final int speakLanguage;

    @NotNull
    @Min(message = "상대방의 학습 언어 이름을 올바르게 입력해주세요.", value = 1)
    private final int learningLanguage;

    @NotNull(message = "사용자 검색 시 상대방이 학습하고 있는 언어에 대한 레벨정보는 필수로 입력하셔야 합니다.")
    private final Set<LanguageLevel> learningLanguageLevel;
}
