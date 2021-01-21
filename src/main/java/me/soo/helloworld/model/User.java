package me.soo.helloworld.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.*;
import java.util.Date;

/** Key Assignment
 * 1. How can it let only certain data (like some letters and special characters) inserted
 * 2. How to Handle Empty Data inserted?
 * */

@Getter @Setter
@Builder @AllArgsConstructor
public class User {
    @NotBlank(message = "아이디를 입력해주세요. (5 ~ 20자 이내, 영문 대/소문자 혹은 숫자만 허용)")
    @Pattern(regexp = "^[0-9a-zA-Z]{5,20}$")
    private final String userId;

    @NotBlank(message = "비밀번호를 입력해주세요 (8 ~ 20자 이내, 영문 대/소문자, 숫자, 특수문자를 각각 1개 이상 포함)")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{8,20}")
    private final String password;

    @Email(message = "이메일 형식에 맞게 입력해주세요")
    @NotBlank
    private final String email;

    @NotBlank(message = "성별을 입력해주세요.")
    private final String gender;

    @NotNull(message = "생년월일을 입력해주세요.")
    private final Date birthday;

    @NotBlank(message = "출신 나라를 입력해주세요.")
    private final String originCountry;

    @NotBlank(message = "현재 거주하고 있는 나라를 입력해주세요.")
    private final String livingCountry;

    @Nullable
    private final String livingTown;

    @Nullable
    private final String aboutMe;
}
