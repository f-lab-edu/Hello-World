package me.soo.helloworld.model.user;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.*;
import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
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

    @NotBlank(message = "성별을 입력해주세요. 'M'(Male), 'F'(Female), 'O'(Other) 중 하나만 입력이 가능합니다.")
    @Pattern(regexp = "^[MFO]$")
    private final String gender;

    @NotNull(message = "생년월일을 입력해주세요.")
    private final Date birthday;

    @NotNull(message = "출신 나라를 입력해주세요.")
    private final int originCountry;

    @NotNull(message = "현재 거주하고 있는 나라를 입력해주세요.")
    private final int livingCountry;

    @Nullable
    private final int livingTown;

    @Nullable
    private final String aboutMe;

    @Nullable
    private final String profileImageName;

    @Nullable
    private final String profileImagePath;

    /**
     * 객체의 생성과 구성을 담당하는 책임을 User 클래스 자체로 가지고 있다고 판단
     * 따라서 암호화가 된 비밀번호를 가진 User 객체의 생성을 UserService 가 아닌  User 클래스 자체가 담당하도록 책임 분리
     * @param encodedPassword
     * @return User
     */
    public User buildUserWithEncodedPassword(String encodedPassword) {
        return User.builder()
                .userId(this.userId)
                .password(encodedPassword)
                .email(this.email)
                .gender(this.gender)
                .birthday(this.birthday)
                .originCountry(this.originCountry)
                .livingCountry(this.livingCountry)
                .livingTown(this.livingTown)
                .aboutMe(this.aboutMe)
                .build();
    }
}
