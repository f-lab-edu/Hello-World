package me.soo.helloworld.service.user;

import me.soo.helloworld.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import javax.validation.*;
import java.sql.Date;
import java.util.Set;

import static me.soo.helloworld.TestCountries.SOUTH_KOREA;
import static me.soo.helloworld.TestCountries.UNITED_KINGDOM;
import static me.soo.helloworld.TestTowns.NEWCASTLE;
import static org.junit.jupiter.api.Assertions.*;

class UserSignUpValidationTest {
    private Validator validator;

    private User getUserWithVariousName(String name) {
        return User.builder()
                .userId(name)
                .password("!@#$!@#$Gomsu1045")
                .email("test@test.com")
                .gender("M")
                .birthday(Date.valueOf("1993-09-25"))
                .originCountry(SOUTH_KOREA)
                .livingCountry(UNITED_KINGDOM)
                .livingTown(NEWCASTLE)
                .aboutMe("Hello, I'd love to make great friends here")
                .build();
    }

    private User getUserWithVariousPassword(String password) {
        return User.builder()
                .userId("gomsu1045")
                .password(password)
                .email("test@test.com")
                .gender("M")
                .birthday(Date.valueOf("1993-09-25"))
                .originCountry(SOUTH_KOREA)
                .livingCountry(UNITED_KINGDOM)
                .livingTown(NEWCASTLE)
                .aboutMe("Hello, I'd love to make great friends here")
                .build();
    }

    private User getUserWithVariousEmail(String email) {
        return User.builder()
                .userId("Gomsu1045")
                .password("!@#$!@#$Gomsu1045")
                .email(email)
                .gender("M")
                .birthday(Date.valueOf("1993-09-25"))
                .originCountry(SOUTH_KOREA)
                .livingCountry(UNITED_KINGDOM)
                .livingTown(NEWCASTLE)
                .aboutMe("Hello, I'd love to make great friends here")
                .build();
    }

    @BeforeEach
    public void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("정상적인 아이디를 넣으면 통과합니다.")
    public void userIdWithSuccess() {
        String userId = "msugo1045";
        User testUser = getUserWithVariousName(userId);
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("공백을 userId에 넣으면 validation 위반이 발생합니다.")
    public void userIdWithAnyBlankFail() {
        String userId = "test   ";
        User testUser = getUserWithVariousName(userId);
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("빈 문자열을 userId에 넣으면 validation 위반이 발생합니다.")
    public void userIdWithEmptyStringFail() {
        String userId = "";
        User testUser = getUserWithVariousName(userId);
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("null을 userId에 넣으면 validation 위반이 발생합니다.")
    public void userIdWithNullFail() {
        String userId = null;
        User testUser = getUserWithVariousName(userId);
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("특수문자를 userId에 넣으면 validation 위반이 발생합니다.")
    public void userIdWithSpecialCharsFail() {
        String userId = "!@#$test";
        User testUser = getUserWithVariousName(userId);
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("입력 값이 제한된 최대 길이를 초과한 경우 userId에 넣으면 validation 위반이 발생합니다.")
    public void userIdOverLengthLimitFail() {
        String userId = "testtttttttttttttttttttttttttttttttttttttttttttt";
        User testUser = getUserWithVariousName(userId);
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("정상적인 암호를 넣으면 통과합니다.")
    public void userPasswordWithSuccess() {
        String password = "Gomsu!0$%1045";
        User testUser = getUserWithVariousPassword(password);
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("공백을 password 에 넣으면 validation 위반이 발생합니다.")
    public void userPasswordWithBlankFail() {
        String password = "            ";
        User testUser = getUserWithVariousPassword(password);
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("빈 문자열을 password 에 넣으면 validation 위반이 발생합니다.")
    public void userPasswordWithEmptyStringFail() {
        String password = "";
        User testUser = getUserWithVariousPassword(password);
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("null을 password 에 넣으면 validation 위반이 발생합니다.")
    public void userPasswordWithNullFail() {
        String password = null;
        User testUser = getUserWithVariousPassword(password);
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("password에 소문자가 적어도 한 자 이상 들어가지 않으면 validation 위반이 발생합니다.")
    public void userPasswordWithNotAnyLowercaseFail() {
        String password = "GOMSU!0$%1045";
        User testUser = getUserWithVariousPassword(password);
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("password에 대문자가 적어도 한 자 이상 들어가지 않으면 validation 위반이 발생합니다.")
    public void userPasswordWithNotAnyUppercaseFail() {
        String password = "gomsu!0$%1045";
        User testUser = getUserWithVariousPassword(password);
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("password에 숫자가 적어도 한 자 이상 들어가지 않으면 validation 위반이 발생합니다.")
    public void userPasswordWithNotAnyNumberFail() {
        String password = "Gomsu!@#!@#";
        User testUser = getUserWithVariousPassword(password);
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("password에 특수문자가 적어도 한 글자 이상 들어가지 않으면 validation 위반이 발생합니다.")
    public void userPasswordWithNotAnySpecialCaseFail() {
        String password = "Gomsu1045gOmsu1045";
        User testUser = getUserWithVariousPassword(password);
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("입력받은 password가 제한된 길이를 초과한 경우 validation 위반이 발생합니다.")
    public void userPasswordOverLengthLimitFail() {
        String password = "1234!@#$asdfASDF1234!@#$asdfASDF";
        User testUser = getUserWithVariousPassword(password);
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("email에 정상적인 형식의 이메일 넣으면 통과합니다.")
    public void userEmailWithSuccess() {
        String email = "test@test.com";
        User testUser = getUserWithVariousEmail(email);
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("email에 잘못된 이메일 형식을 입력하는 경우 validation 위반이 발생합니다.")
    public void userEmailWithInvalidFormatFail() {
        String email = "test@";
        User testUser = getUserWithVariousEmail(email);
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("email에 빈 문자열을 입력하는 경우 validation 위반이 발생합니다.")
    public void userEmailWithEmptyStringFail() {
        String email = "";
        User testUser = getUserWithVariousEmail(email);
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("email에 null을 입력하는 경우 validation 위반이 발생합니다.")
    public void userEmailWithNullFail() {
        String password = null;
        User testUser = getUserWithVariousPassword(password);
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("email에 빈 문자열을 입력하는 경우 validation 위반이 발생합니다.")
    public void userEmailWithBlankFail() {
        String email = "            ";
        User testUser = getUserWithVariousEmail(email);
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);
        assertFalse(violations.isEmpty());
    }

}
