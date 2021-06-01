package me.soo.helloworld.service.login;

import me.soo.helloworld.exception.DuplicateLoginRequestException;
import me.soo.helloworld.model.user.UserLoginData;
import me.soo.helloworld.model.user.LoginRequest;
import me.soo.helloworld.service.SessionLoginService;
import me.soo.helloworld.service.UserService;
import me.soo.helloworld.util.constant.SessionKeys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpSession;

import static me.soo.helloworld.TestUsersFixture.CURRENT_USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    UserLoginData testUserLoginData;

    @InjectMocks
    SessionLoginService loginService;

    @Spy
    HttpSession httpSession = new MockHttpSession();

    @Mock
    UserService userService;

    /*
        Basic User Fixture

        new User(
            userId= "soo1045",
            password= "soo1045",
            email= "soo1045@gmail.com",
            gender= "M",
            birthday= Date.valueOf("2009-01-01"),
            originCountry= SOUTH_KOREA,
            livingCountry= UNITED_KINGDOM,
            livingTown= NEWCASTLE
        );
    */
    @BeforeEach
    public void setUp() {
        testUserLoginData = UserLoginData.builder()
                .userId(CURRENT_USER.getUserId())
                .password(CURRENT_USER.getPassword())
                .build();
    }

    @Test
    @DisplayName("DB에 등록된 회원정보와 일치하는 로그인 요청이 오면 로그인에 성공합니다.")
    public void successLoginRequestWithCorrectLoginRequest() {
        LoginRequest loginRequest = new LoginRequest(
                CURRENT_USER.getUserId(),
                CURRENT_USER.getPassword()
        );

        when(userService.getUserLoginInfo(loginRequest.getUserId(), loginRequest.getPassword())).thenReturn(testUserLoginData);

        loginService.login(loginRequest);

        assertEquals(httpSession.getAttribute(SessionKeys.USER_ID), CURRENT_USER.getUserId());
    }

    @Test
    @DisplayName("이미 로그인 된 회원에게서 또 다시 로그인 요청이 오면 DuplicateLoginRequestException이 발생합니다.")
    public void failLoginRequestWithWrongLoginRequest() {
        LoginRequest loginRequest = new LoginRequest(
                CURRENT_USER.getUserId(),
                CURRENT_USER.getPassword()
        );

        httpSession.setAttribute(SessionKeys.USER_ID, loginRequest.getUserId());

        assertThrows(DuplicateLoginRequestException.class, () -> {
            loginService.login(loginRequest);
        });
    }

    @Test
    @DisplayName("로그인 된 회원을 로그아웃 시키는데 성공합니다.")
    public void successLogoutRequest() {
        loginService.logout();
    }
}
