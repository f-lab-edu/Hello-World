package me.soo.helloworld.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Builder
@AllArgsConstructor
public class UserUpdateRequest {

    @Nullable
    @Pattern(message = "성별은 'M'(Male), 'F'(Female), 'O'(Other) 중 하나만 입력이 가능합니다.", regexp = "^[MFO]$")
    private final String gender;

    @Nullable
    private final Integer livingCountry;

    @Nullable
    private final Integer livingTown;

    @Nullable
    private final String aboutMe;

}
