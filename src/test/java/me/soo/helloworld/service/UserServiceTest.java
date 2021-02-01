package me.soo.helloworld.service;

import me.soo.helloworld.exception.IncorrectUserInfoException;
import me.soo.helloworld.model.user.UserLoginRequest;
import me.soo.helloworld.model.user.User;
import me.soo.helloworld.model.user.UserPasswordUpdateRequest;
import me.soo.helloworld.repository.UserRepository;
import me.soo.helloworld.util.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    User testUser;

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        testUser = new User(
                "Soo",
                "Bakery",
                "test@test.com",
                "Male",
                Date.valueOf("1993-09-25"),
                "South Korea",
                "The United Kingdom",
                "Newcastle Upon Tyne",
                ""
        );
    }

    @Test
    @DisplayName("조회한 아이디가 DB에 존재하지 않는 경우 false를 리턴합니다.")
    public void duplicateUserIdExceptionFalse() {
        when(userRepository.isUserIdDuplicate(testUser.getUserId())).thenReturn(false);

        assertThat(userService.isUserIdDuplicate(testUser.getUserId()), is(false));

        verify(userRepository, times(1)).isUserIdDuplicate(testUser.getUserId());
    }

    @Test
    @DisplayName("조회한 아이디가 DB에 존재하는 경우 true를 리턴합니다.")
    public void duplicateUserIdExceptionTrue() {
        when(userRepository.isUserIdDuplicate(testUser.getUserId())).thenReturn(true);

        assertThat(userService.isUserIdDuplicate(testUser.getUserId()), is(true));

        verify(userRepository, times(1)).isUserIdDuplicate(testUser.getUserId());
    }

    @Test
    @DisplayName("LoginRequest를 통해 DB에 보관되어 있는 사용자의 ID와 일치하는 ID와 Password를 보낼 경우 그에 맞는 사용자를 리턴하는데 성공합니다.")
    public void getUserWithCorrectLoginRequestSuccess() {
        UserLoginRequest loginRequest = new UserLoginRequest(testUser.getUserId(), "Typo is everywhere~");

        when(userRepository.getUserById(loginRequest.getUserId())).thenReturn(testUser);
        when(passwordEncoder.isMatch(loginRequest.getPassword(), testUser.getPassword())).thenReturn(true);

        userService.getUser(loginRequest);

        verify(userRepository, times(1)).getUserById(loginRequest.getUserId());
        verify(passwordEncoder, times(1)).isMatch(loginRequest.getPassword(), testUser.getPassword());
    }

    @Test
    @DisplayName("LoginRequest를 통해 받은 유저 ID가 존재하지 않는 경우 IncorrectUserInfoException 이 발생합니다.")
    public void getUserWithWrongIdFail() {
        UserLoginRequest loginRequest = new UserLoginRequest("I'm a wrong user", testUser.getPassword());

        when(userRepository.getUserById(loginRequest.getUserId())).thenReturn(null);

        assertThrows(IncorrectUserInfoException.class, () -> {
           userService.getUser(loginRequest);
        });

        verify(userRepository, times(1)).getUserById(loginRequest.getUserId());
    }

    @Test
    @DisplayName("LoginRequest를 통해 받은 유저의 Password가 일치하지 않는 경우 IncorrectUserInfoException 이 발생합니다.")
    public void getUserWithWrongPasswordFail() {
        UserLoginRequest loginRequest = new UserLoginRequest(testUser.getUserId(), "Typo is everywhere~");

        when(userRepository.getUserById(loginRequest.getUserId())).thenReturn(testUser);
        when(passwordEncoder.isMatch(loginRequest.getPassword(), testUser.getPassword())).thenReturn(false);

        assertThrows(IncorrectUserInfoException.class, () -> {
            userService.getUser(loginRequest);
        });

        verify(userRepository, times(1)).getUserById(loginRequest.getUserId());
        verify(passwordEncoder, times(1)).isMatch(loginRequest.getPassword(), testUser.getPassword());
    }
}