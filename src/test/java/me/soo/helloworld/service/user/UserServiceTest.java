package me.soo.helloworld.service.user;

import me.soo.helloworld.exception.InvalidUserInfoException;
import me.soo.helloworld.mapper.UserMapper;
import me.soo.helloworld.model.email.EmailBase;
import me.soo.helloworld.model.email.FindPasswordEmail;
import me.soo.helloworld.model.user.FindPasswordRequest;
import me.soo.helloworld.model.user.UserLoginData;
import me.soo.helloworld.model.user.LoginRequest;
import me.soo.helloworld.service.EmailService;
import me.soo.helloworld.service.UserService;
import me.soo.helloworld.util.encoder.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;
import java.util.UUID;

import static me.soo.helloworld.TestUsersFixture.CURRENT_USER;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    UserLoginData testUserLoginData;

    @InjectMocks
    UserService userService;

    @Mock
    UserMapper userMapper;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    EmailService emailService;

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
    @DisplayName("조회한 아이디가 DB에 존재하지 않는 경우 false를 리턴합니다.")
    public void duplicateUserIdExceptionFalse() {
        when(userMapper.isUserIdExist(CURRENT_USER.getUserId())).thenReturn(false);

        assertThat(userService.isUserIdExist(CURRENT_USER.getUserId()), is(false));

        verify(userMapper, times(1)).isUserIdExist(CURRENT_USER.getUserId());
    }

    @Test
    @DisplayName("조회한 아이디가 DB에 존재하는 경우 true를 리턴합니다.")
    public void duplicateUserIdExceptionTrue() {
        when(userMapper.isUserIdExist(CURRENT_USER.getUserId())).thenReturn(true);

        assertThat(userService.isUserIdExist(CURRENT_USER.getUserId()), is(true));

        verify(userMapper, times(1)).isUserIdExist(CURRENT_USER.getUserId());
    }

    @Test
    @DisplayName("유저 정보를 얻기 위해 요청 받은 아이디와 비밀번호가 DB에 저장되어 있는 사용자의 아이디와 비밀번호에 일치하는 경우 사용자를 리턴하는데 성공합니다.")
    public void getUserWithCorrectIdAndPasswordSuccess() {
        LoginRequest loginRequest = new LoginRequest(
                CURRENT_USER.getUserId(),
                CURRENT_USER.getPassword()
        );

        when(userMapper.getUserLoginDataById(loginRequest.getUserId())).thenReturn(Optional.ofNullable(testUserLoginData));
        when(passwordEncoder.isMatch(loginRequest.getPassword(), CURRENT_USER.getPassword())).thenReturn(true);

        userService.getUserLoginInfo(loginRequest.getUserId(), loginRequest.getPassword());

        verify(userMapper, times(1)).getUserLoginDataById(loginRequest.getUserId());
        verify(passwordEncoder, times(1)).isMatch(loginRequest.getPassword(), CURRENT_USER.getPassword());
    }

    @Test
    @DisplayName("유저 정보를 얻기 위해 요청 받은 아이디가 DB에 존재하지 않는 경우 InvalidUserInfoException 이 발생하며 테스트에 실패합니다.")
    public void getUserWithWrongIdFail() {
        LoginRequest loginRequest = new LoginRequest(
                "I'm a wrong user.",
                CURRENT_USER.getPassword()
        );

        when(userMapper.getUserLoginDataById(loginRequest.getUserId())).thenReturn(Optional.empty());

        assertThrows(InvalidUserInfoException.class, () -> {
           userService.getUserLoginInfo(loginRequest.getUserId(), loginRequest.getPassword());
        });

        verify(userMapper, times(1)).getUserLoginDataById(loginRequest.getUserId());
    }

    @Test
    @DisplayName("유저 정보를 얻기 위해 요청 받은 비밀번호가 일치하지 않는 경우 InvalidUserInfoException 이 발생하며 테스트에 실패합니다.")
    public void getUserWithWrongPasswordFail() {
        LoginRequest loginRequest = new LoginRequest(
                CURRENT_USER.getUserId(),
                "Typo is everywhere ~."
        );

        when(userMapper.getUserLoginDataById(loginRequest.getUserId())).thenReturn(Optional.ofNullable(testUserLoginData));
        when(passwordEncoder.isMatch(loginRequest.getPassword(), CURRENT_USER.getPassword())).thenReturn(false);

        assertThrows(InvalidUserInfoException.class, () -> {
            userService.getUserLoginInfo(loginRequest.getUserId(), loginRequest.getPassword());
        });

        verify(userMapper, times(1)).getUserLoginDataById(loginRequest.getUserId());
        verify(passwordEncoder, times(1)).isMatch(loginRequest.getPassword(), CURRENT_USER.getPassword());
    }

    @MockitoSettings(strictness = Strictness.LENIENT)
    @Test
    @DisplayName("올바른 아이디와 이메일로 비밀번호 찾기를 요청한 경우 임시 비밀번호가 이메일로 전송되고, DB 정보에도 업데이트 됩니다.")
    public void findUserPasswordSuccess() {
        FindPasswordRequest findPasswordRequest = new FindPasswordRequest(CURRENT_USER.getUserId(), CURRENT_USER.getEmail());
        when(userMapper.isEmailValid(findPasswordRequest)).thenReturn(true);

        String temporaryPassword = UUID.randomUUID().toString();
        EmailBase email = FindPasswordEmail.create(findPasswordRequest.getEmail(), temporaryPassword);
        doNothing().when(emailService).sendEmail(email);

        String encodedPassword = UUID.randomUUID().toString();
        when(passwordEncoder.encode(temporaryPassword)).thenReturn(encodedPassword);
        doNothing().when(userMapper).updateUserPassword(findPasswordRequest.getUserId(), encodedPassword);

        userService.findPassword(findPasswordRequest);
    }

    @Test
    @DisplayName("비밀번호 찾기를 위해 요청받은 사용자 ID가 존재하지 않는 경우 InvalidUserInfoException 이 발생합니다.")
    public void findUserPasswordFailWithWrongID() {
        FindPasswordRequest findPasswordRequest = new FindPasswordRequest("HahaWrongId", CURRENT_USER.getEmail());
        when(userMapper.isEmailValid(findPasswordRequest)).thenReturn(false);

        assertThrows(InvalidUserInfoException.class, () -> {
            userService.findPassword(findPasswordRequest);
        });

        verify(userMapper, times(1)).isUserEmailExist(findPasswordRequest);
    }

    @Test
    @DisplayName("비밀번호 찾기를 위해 요청받은 사용자의 이메일이 DB에 있는 정보와 일치하지 않는 경우 InvalidUserInfoException 이 발생합니다.")
    public void findUserPasswordFailWithWrongEmail() {
        FindPasswordRequest findPasswordRequest = new FindPasswordRequest(CURRENT_USER.getUserId(), "some@wrong.email");
        when(userMapper.isEmailValid(findPasswordRequest)).thenReturn(false);

        assertThrows(InvalidUserInfoException.class, () -> {
            userService.findPassword(findPasswordRequest);
        });

        verify(userMapper, times(1)).isUserEmailExist(findPasswordRequest);
    }
}