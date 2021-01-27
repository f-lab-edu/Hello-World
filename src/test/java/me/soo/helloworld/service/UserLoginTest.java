package me.soo.helloworld.service;

import com.sun.jdi.request.DuplicateRequestException;
import me.soo.helloworld.mapper.UserMapper;
import me.soo.helloworld.model.user.UserLoginInfo;
import me.soo.helloworld.repository.UserRepository;
import me.soo.helloworld.util.PasswordEncoder;
import me.soo.helloworld.util.PasswordEncoderBcrypt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserLoginTest {
    UserLoginInfo correctTestUser;
    UserLoginInfo correctTestUserWithWrongPassword;
    UserLoginInfo wrongTestUser;

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    MockHttpSession httpSession;

    @BeforeEach
    public void setUp() {
        httpSession = new MockHttpSession();
        correctTestUser = new UserLoginInfo("Soo", "Soo");
        correctTestUserWithWrongPassword = new UserLoginInfo("Soo","Bakery");
        wrongTestUser = new UserLoginInfo("Bakery", "Bakery");
    }

    @Test
    @DisplayName("DB에 등록된 정보와 일치하는 정보를 입력하면 로그인에 성공합니다.")
    public void successLoginRequestWithRegisteredId() {
        when(userRepository.getRegisteredUserInfo(correctTestUser)).thenReturn(correctTestUser);
        when(passwordEncoder.isMatch(correctTestUser.getPassword(), correctTestUser.getPassword())).thenReturn(true);

        userService.loginRequest(correctTestUser, httpSession);
        assertEquals(httpSession.getAttribute("userId"), correctTestUser.getUserId());

        verify(userRepository, times(1)).getRegisteredUserInfo(correctTestUser);
        verify(passwordEncoder, times(1)).isMatch(correctTestUser.getPassword(),
                correctTestUser.getPassword());
    }

    @Test
    @DisplayName("존재하지 않는 아이디를 입력하면 IllegalArgumentException 이 발생합니다.")
    public void failLoginRequestWithNotValidId() {
        when(userRepository.getRegisteredUserInfo(wrongTestUser)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.loginRequest(wrongTestUser, httpSession);
        });

        verify(passwordEncoder, times(0)).isMatch(wrongTestUser.getPassword(), null);
    }

    @Test
    @DisplayName("잘못된 패스워드를 입력하면 IllegalArgumentException 이 발생합니다.")
    public void failLoginRequestWithWrongPassword() {
        given(userRepository.getRegisteredUserInfo(correctTestUserWithWrongPassword)).willReturn(correctTestUser);
        when(passwordEncoder.isMatch(correctTestUserWithWrongPassword.getPassword(),
                correctTestUser.getPassword())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.loginRequest(correctTestUserWithWrongPassword, httpSession);
        });

        verify(userRepository, times(1)).getRegisteredUserInfo(correctTestUserWithWrongPassword);
        verify(passwordEncoder, times(1)).isMatch(correctTestUserWithWrongPassword.getPassword(),
                correctTestUser.getPassword());
    }

    @Test
    @DisplayName("이미 로그인한 회원의 경우 DuplicateRequestException 이 발생합니다.")
    public void failLoginRequestWithAlreadyLoginId() {
        httpSession.setAttribute("userId", correctTestUser.getUserId());

        assertThrows(DuplicateRequestException.class, () -> {
            userService.loginRequest(correctTestUser, httpSession);
        });

        verify(userRepository, times(0)).getRegisteredUserInfo(correctTestUser);
        verify(passwordEncoder, times(0)).isMatch(correctTestUser.getPassword(),
                correctTestUser.getPassword());
    }

    @Test
    @DisplayName("로그인 된 회원을 로그아웃 시키는데 성공합니다.")
    public void successLogoutRequest() {
        userService.logoutRequest(httpSession);
    }
}
