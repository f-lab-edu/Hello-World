package me.soo.helloworld.service;

import com.sun.jdi.request.DuplicateRequestException;
import me.soo.helloworld.model.user.UserLoginInfo;
import me.soo.helloworld.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserLoginTest {

    UserLoginInfo testUser;

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    MockHttpSession httpSession;

    @BeforeEach
    public void setUp() {
        httpSession = new MockHttpSession();
        testUser = new UserLoginInfo("Soo", "Soo");
    }


    @Test
    @DisplayName("DB에 등록된 정보와 일치하는 정보를 입력하면 로그인에 성공합니다.")
    public void successLoginRequestWithRegisteredId() {
        when(userRepository.isRegisteredUser(testUser)).thenReturn(true);
        userService.loginRequest(testUser, httpSession);
        assertEquals(httpSession.getAttribute("userId"), testUser.getUserId());
    }

    @Test
    @DisplayName("존재하지 않는 아이디를 입력하면 IllegalArgumentException 이 발생합니다.")
    public void failLoginRequestWithNotValidId() {
        when(userRepository.isRegisteredUser(testUser)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.loginRequest(testUser, httpSession);
        });
    }

    @Test
    @DisplayName("이미 로그인한 회원의 경우 DuplicateRequestException 이 발생합니다.")
    public void failLoginRequestWithAlreadyLoginId() {
        httpSession.setAttribute("userId", testUser.getUserId());

        assertThrows(DuplicateRequestException.class, () -> {
            userService.loginRequest(testUser, httpSession);
        });
    }

    @Test
    @DisplayName("로그인 된 회원을 로그아웃 시키는데 성공합니다.")
    public void successLogoutRequest() {
        System.out.println(httpSession.getId());
        httpSession.setAttribute("userId", testUser.getUserId());
        userService.logoutRequest(httpSession);
    }
}
