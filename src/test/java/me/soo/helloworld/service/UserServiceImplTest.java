package me.soo.helloworld.service;

import me.soo.helloworld.model.User;
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

import java.sql.Date;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    User testUser;

    @InjectMocks
    UserServiceImpl userService;

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

        passwordEncoder = new PasswordEncoderBcrypt();
    }

    @Test
    public void insertUserServiceTest() {
        userService.userSignUp(testUser);
    }

    @Test
    @DisplayName("조회한 아이디가 DB에 존재하지 않는 경우 false를 리턴합니다.")
    public void duplicateUserIdExceptionFalse() {
        when(userRepository.isUserIdDuplicate(testUser.getUserId())).thenReturn(false);
        assertThat(userService.isUserIdDuplicate(testUser.getUserId()), is(false));

        userRepository.isUserIdDuplicate(testUser.getUserId());
    }

    @Test
    @DisplayName("조회한 아이디가 DB에 존재하는 경우 true를 리턴합니다.")
    public void duplicateUserIdExceptionTrue() {
        when(userRepository.isUserIdDuplicate(testUser.getUserId())).thenReturn(true);
        assertThat(userService.isUserIdDuplicate(testUser.getUserId()), is(true));

        userRepository.isUserIdDuplicate(testUser.getUserId());
    }
}