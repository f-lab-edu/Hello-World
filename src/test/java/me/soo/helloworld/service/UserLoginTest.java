package me.soo.helloworld.service;

import com.sun.jdi.request.DuplicateRequestException;
import me.soo.helloworld.model.user.UserLoginInfo;
import me.soo.helloworld.util.PasswordEncoder;
import me.soo.helloworld.util.SessionKeys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class UserLoginTest {

    UserLoginInfo testUser;

    UserLoginInfo correctLoginInfo;

    UserLoginInfo wrongPasswordLoginInfo;

    UserLoginInfo wrongIdLoginInfo;

    @InjectMocks
    SessionLoginService loginService;

    @Mock
    PasswordEncoder passwordEncoder;

    MockHttpSession httpSession;

    @BeforeEach
    public void setUp() {

        httpSession = new MockHttpSession();

        testUser = new UserLoginInfo("Soo", "Soo");

        correctLoginInfo = new UserLoginInfo("Soo", passwordEncoder.encode(testUser.getPassword()));

        wrongPasswordLoginInfo = new UserLoginInfo("Soo","Bakery");

        wrongIdLoginInfo = new UserLoginInfo("Bakery", "Bakery");

    }

    @Test
    @DisplayName("DB에 등록된 정보와 일치하는 정보를 입력하면 로그인에 성공합니다.")
    public void successLoginRequestWithRegisteredId() {

        when(passwordEncoder.isMatch(testUser.getPassword(), correctLoginInfo.getPassword()))
                .thenReturn(true);

        loginService.login(testUser, correctLoginInfo, httpSession);

        assertEquals(httpSession.getAttribute(SessionKeys.USER_ID), testUser.getUserId());
    }

    @Test
    @DisplayName("존재하지 않는 아이디를 입력하면 IllegalArgumentException 이 발생합니다.")
    public void failLoginRequestWithNotValidId() {

        assertThrows(IllegalArgumentException.class, () -> {
            loginService.login(testUser, wrongIdLoginInfo ,httpSession);
        });
    }

    @Test
    @DisplayName("잘못된 패스워드를 입력하면 IllegalArgumentException 이 발생합니다.")
    public void failLoginRequestWithWrongPassword() {

        assertThrows(IllegalArgumentException.class, () -> {
            loginService.login(testUser, wrongPasswordLoginInfo, httpSession);
        });

    }

    @Test
    @DisplayName("이미 로그인한 회원의 경우 DuplicateRequestException 이 발생합니다.")
    public void failLoginRequestWithAlreadyLoginId() {

        httpSession.setAttribute(SessionKeys.USER_ID, testUser.getUserId());

        assertThrows(DuplicateRequestException.class, () -> {
            loginService.login(testUser, correctLoginInfo, httpSession);
        });
    }

    @Test
    @DisplayName("로그인 된 회원을 로그아웃 시키는데 성공합니다.")
    public void successLogoutRequest() {

        loginService.logout(httpSession);
    }
}
