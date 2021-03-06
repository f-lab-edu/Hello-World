package me.soo.helloworld.service.user;

import me.soo.helloworld.exception.InvalidUserInfoException;
import me.soo.helloworld.model.email.EmailBase;
import me.soo.helloworld.model.email.FindPasswordEmail;
import me.soo.helloworld.model.user.UserFindPasswordRequest;
import me.soo.helloworld.model.user.UserLoginRequest;
import me.soo.helloworld.model.user.User;
import me.soo.helloworld.repository.UserRepository;
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

import java.sql.Date;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
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

    @Mock
    EmailService emailService;

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
    @DisplayName("조회한 아이디가 DB에 존재하지 않는 경우 false를 리턴합니다.")
    public void duplicateUserIdExceptionFalse() {
        when(userRepository.isUserIdDuplicate(testUser.getUserId())).thenReturn(false);

        assertThat(userService.isUserIdExist(testUser.getUserId()), is(false));

        verify(userRepository, times(1)).isUserIdDuplicate(testUser.getUserId());
    }

    @Test
    @DisplayName("조회한 아이디가 DB에 존재하는 경우 true를 리턴합니다.")
    public void duplicateUserIdExceptionTrue() {
        when(userRepository.isUserIdDuplicate(testUser.getUserId())).thenReturn(true);

        assertThat(userService.isUserIdExist(testUser.getUserId()), is(true));

        verify(userRepository, times(1)).isUserIdDuplicate(testUser.getUserId());
    }

    @Test
    @DisplayName("LoginRequest를 통해 DB에 보관되어 있는 사용자의 ID와 일치하는 ID와 Password를 보낼 경우 그에 맞는 사용자를 리턴하는데 성공합니다.")
    public void getUserWithCorrectLoginRequestSuccess() {
        UserLoginRequest loginRequest = new UserLoginRequest(testUser.getUserId(), "Typo is everywhere~");

        when(userRepository.getUserById(loginRequest.getUserId())).thenReturn(testUser);
        when(passwordEncoder.isMatch(loginRequest.getPassword(), testUser.getPassword())).thenReturn(true);

        userService.getUser(loginRequest.getUserId(), loginRequest.getPassword());

        verify(userRepository, times(1)).getUserById(loginRequest.getUserId());
        verify(passwordEncoder, times(1)).isMatch(loginRequest.getPassword(), testUser.getPassword());
    }

    @Test
    @DisplayName("요청정보에 해당하는 유저의 아이디가 존재하지 않는 경우 InvalidUserInfoException 이 발생합니다.")
    public void getUserWithWrongIdFail() {
        UserLoginRequest loginRequest = new UserLoginRequest("I'm a wrong user", testUser.getPassword());

        when(userRepository.getUserById(loginRequest.getUserId())).thenReturn(null);

        assertThrows(InvalidUserInfoException.class, () -> {
           userService.getUser(loginRequest.getUserId(), loginRequest.getPassword());
        });

        verify(userRepository, times(1)).getUserById(loginRequest.getUserId());
    }

    @Test
    @DisplayName("요청정보에 해당하는 유저의 비밀번호가 일치하지 않는 경우 InvalidUserInfoException 이 발생합니다.")
    public void getUserWithWrongPasswordFail() {
        UserLoginRequest loginRequest = new UserLoginRequest(testUser.getUserId(), "Typo is everywhere~");

        when(userRepository.getUserById(loginRequest.getUserId())).thenReturn(testUser);
        when(passwordEncoder.isMatch(loginRequest.getPassword(), testUser.getPassword())).thenReturn(false);

        assertThrows(InvalidUserInfoException.class, () -> {
            userService.getUser(loginRequest.getUserId(), loginRequest.getPassword());
        });

        verify(userRepository, times(1)).getUserById(loginRequest.getUserId());
        verify(passwordEncoder, times(1)).isMatch(loginRequest.getPassword(), testUser.getPassword());
    }

    @MockitoSettings(strictness = Strictness.LENIENT)
    @Test
    @DisplayName("올바른 아이디와 이메일로 비밀번호 찾기를 요청한 경우 임시 비밀번호가 이메일로 전송되고, DB 정보에도 업데이트 됩니다.")
    public void findUserPasswordSuccess() {
        UserFindPasswordRequest findPasswordRequest = new UserFindPasswordRequest(testUser.getUserId(), testUser.getEmail());

        // 테스트 편의를 위해 필요한 정보로만 객체 구성
        User userFromDB = User.builder()
                .userId(testUser.getUserId())
                .email(testUser.getEmail()).build();

        when(userRepository.getUserById(findPasswordRequest.getUserId())).thenReturn(userFromDB);

        String temporaryPassword = UUID.randomUUID().toString();

        EmailBase email = FindPasswordEmail.create(findPasswordRequest.getEmail(), temporaryPassword);

        doNothing().when(emailService).sendEmail(email);

        String encodedPassword = UUID.randomUUID().toString();
        when(passwordEncoder.encode(temporaryPassword)).thenReturn(encodedPassword);
        doNothing().when(userRepository).updateUserPassword(userFromDB.getUserId(), encodedPassword);

        userService.findUserPassword(findPasswordRequest);
    }

    @Test
    @DisplayName("비밀번호 찾기를 위해 요청받은 사용자 ID가 존재하지 않는 경우 InvalidUserInfoException 이 발생합니다.")
    public void findUserPasswordFailWithWrongID() {
        UserFindPasswordRequest findPasswordRequest = new UserFindPasswordRequest(testUser.getUserId(), testUser.getEmail());

        when(userRepository.getUserById(findPasswordRequest.getUserId())).thenReturn(null);

        assertThrows(InvalidUserInfoException.class, () -> {
            userService.findUserPassword(findPasswordRequest);
        });

        verify(userRepository, times(1)).getUserById(findPasswordRequest.getUserId());
    }

    @Test
    @DisplayName("비밀번호 찾기를 위해 요청받은 사용자의 이메일이 DB에 있는 정보와 일치하지 않는 경우 InvalidUserInfoException 이 발생합니다.")
    public void findUserPasswordFailWithWrongEmail() {
        UserFindPasswordRequest findPasswordRequest = new UserFindPasswordRequest(testUser.getUserId(), "some@wrong.email");

        User userFromDB = User.builder()
                .userId(testUser.getUserId())
                .email(testUser.getEmail()).build();

        when(userRepository.getUserById(findPasswordRequest.getUserId())).thenReturn(userFromDB);

        assertThrows(InvalidUserInfoException.class, () -> {
            userService.findUserPassword(findPasswordRequest);
        });

        verify(userRepository, times(1)).getUserById(findPasswordRequest.getUserId());
    }
}