package me.soo.helloworld.service;

import com.sun.jdi.request.DuplicateRequestException;
import me.soo.helloworld.model.user.UserIdAndPassword;
import me.soo.helloworld.util.PasswordEncoder;
import me.soo.helloworld.util.PasswordEncoderBcrypt;
import me.soo.helloworld.util.SessionKeys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.*;

public class UserLoginTest {

    UserIdAndPassword testUserIdAndPassword;

    UserIdAndPassword correctUserIdAndPassword;

    UserIdAndPassword correctUserIdWithWrongPassword;

    UserIdAndPassword wrongUserIdAndPassword;

    LoginService loginService;

    PasswordEncoder passwordEncoder;

    HttpSession httpSession;


    /**
     * Mockito 사용이 익숙치 않아서 설정이 잘못되었는지 테스트가 실패로 나와서 수동으로 객체 생성/주입해서 테스트 완료하였습니다.
     * 시간 소요가 너무 많이되어 차후 Mockito에 익숙해지면 테스트 수정할 계획입니다.
     */
    @BeforeEach
    public void setUp() {

        httpSession = new MockHttpSession();

        passwordEncoder = new PasswordEncoderBcrypt();

        loginService = new SessionLoginService(passwordEncoder, httpSession);

        testUserIdAndPassword = new UserIdAndPassword("Soo", "Soo");

        correctUserIdAndPassword = new UserIdAndPassword("Soo", passwordEncoder.encode(testUserIdAndPassword.getPassword()));

        correctUserIdWithWrongPassword = new UserIdAndPassword("Soo","Bakery");

        wrongUserIdAndPassword = new UserIdAndPassword("Bakery", "Bakery");

    }

    @Test
    @DisplayName("DB에 등록된 정보와 일치하는 정보를 입력하면 로그인에 성공합니다.")
    public void successLoginRequestWithRegisteredId() {

        loginService.login(testUserIdAndPassword, correctUserIdAndPassword);

        assertNotNull(httpSession.getAttribute(SessionKeys.USER_ID));
        assertEquals(httpSession.getAttribute(SessionKeys.USER_ID), testUserIdAndPassword.getUserId());

    }

    @Test
    @DisplayName("잘못된 패스워드를 입력하면 IllegalArgumentException 이 발생합니다.")
    public void failLoginRequestWithWrongPassword() {

        assertThrows(IllegalArgumentException.class, () -> {
            loginService.login(testUserIdAndPassword, wrongUserIdAndPassword);
        });
    }

    @Test
    @DisplayName("이미 로그인한 회원의 경우 DuplicateRequestException 이 발생합니다.")
    public void failLoginRequestWithAlreadyLoginId() {

        httpSession.setAttribute(SessionKeys.USER_ID, testUserIdAndPassword.getUserId());

        assertThrows(DuplicateRequestException.class, () -> {
            loginService.login(testUserIdAndPassword, correctUserIdAndPassword);
        });
    }

    @Test
    @DisplayName("로그인 된 회원을 로그아웃 시키는데 성공합니다.")
    public void successLogoutRequest() {

        loginService.logout();
    }
}
