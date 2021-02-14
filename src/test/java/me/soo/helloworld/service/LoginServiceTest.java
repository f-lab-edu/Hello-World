package me.soo.helloworld.service;

import com.sun.jdi.request.DuplicateRequestException;
import me.soo.helloworld.model.user.UserLoginRequest;
import me.soo.helloworld.model.user.User;
import me.soo.helloworld.util.http.SessionKeys;
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

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    User testUser;

    @InjectMocks
    SessionLoginService loginService;

    @Spy
    HttpSession httpSession = new MockHttpSession();

    @Mock
    UserService userService;

    @BeforeEach
    public void setUp() {
        testUser = User.builder()
                .userId("gomsu1045")
                .password("Gomsu1045!0$%")
                .email("test@test.com")
                .gender("Male")
                .birthday(Date.valueOf("1993-09-25"))
                .originCountry("South Korea")
                .livingCountry("United Kingdom")
                .livingTown("Newcastle Upon Tyne")
                .aboutMe("Hello, I'd love to make great friends here")
                .build();

    }

    @Test
    @DisplayName("DB에 등록된 회원정보와 일치하는 로그인 요청이 오면 로그인에 성공합니다.")
    public void successLoginRequestWithCorrectLoginRequest() {

        UserLoginRequest loginRequest = new UserLoginRequest(testUser.getUserId(), testUser.getPassword());
        when(userService.getUser(loginRequest.getUserId(), loginRequest.getPassword())).thenReturn(testUser);

        loginService.login(loginRequest);

        assertEquals(httpSession.getAttribute(SessionKeys.USER_ID), testUser.getUserId());
    }

    @Test
    @DisplayName("이미 로그인 된 회원에게서 또 다시 로그인 요청이 오면 DuplicateRequestException이 발생합니다.")
    public void failLoginRequestWithWrongLoginRequest() {

        UserLoginRequest loginRequest = new UserLoginRequest(testUser.getUserId(), testUser.getPassword());

        httpSession.setAttribute(SessionKeys.USER_ID, loginRequest.getUserId());

        assertThrows(DuplicateRequestException.class, () -> {
            loginService.login(loginRequest);
        });

    }

    @Test
    @DisplayName("로그인 된 회원을 로그아웃 시키는데 성공합니다.")
    public void successLogoutRequest() {
        loginService.logout();
    }
}
