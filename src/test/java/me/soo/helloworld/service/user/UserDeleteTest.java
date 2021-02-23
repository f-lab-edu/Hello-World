package me.soo.helloworld.service.user;

import me.soo.helloworld.exception.InvalidUserInfoException;
import me.soo.helloworld.model.user.User;
import me.soo.helloworld.repository.UserRepository;
import me.soo.helloworld.service.UserService;
import me.soo.helloworld.util.encoder.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserDeleteTest {

    @InjectMocks
    UserService userService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserRepository userRepository;

    @Mock
    MockHttpSession httpSession;

    User testUser;

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
    @DisplayName("요청받은 비밀번호가 현재 사용자의 비밀번호와 일치하면 해당 회원의 계정을 삭제하고 로그아웃 시키는데 성공합니다.")
    public void userDeleteAccountTestSuccess() {
        String userId = testUser.getUserId();
        String password = testUser.getPassword();

        when(userRepository.getUserPasswordById(userId)).thenReturn(testUser.getPassword());
        when(passwordEncoder.isMatch(password, testUser.getPassword())).thenReturn(true);

        doNothing().when(userRepository).deleteUser(userId);

        userService.userDeleteAccount(userId, password);

        verify(userRepository, times(1)).getUserPasswordById(userId);
        verify(passwordEncoder,times(1)).isMatch(password, testUser.getPassword());
        verify(userRepository, times(1)).deleteUser(userId);
    }

    @Test
    @DisplayName("요청받은 비밀번호가 현재 사용자의 비밀번호와 일치하지 않으면 InvalidUserInfoException 이 발생하며 해당 회원의 계정삭제가 실패합니다.")
    public void userDeleteAccountTestFailWithWrongPassword() {
        String userId = testUser.getUserId();
        String password = "I'm gonna take my horse to the old town road~";

        when(userRepository.getUserPasswordById(userId)).thenReturn(testUser.getPassword());
        when(passwordEncoder.isMatch(password, testUser.getPassword())).thenReturn(false);

        assertThrows(InvalidUserInfoException.class, () -> {
            userService.userDeleteAccount(userId, password);
        });

        verify(userRepository, times(1)).getUserPasswordById(userId);
        verify(passwordEncoder,times(1)).isMatch(password, testUser.getPassword());
    }

    @Test
    @DisplayName("이미 삭제 처리된 사용자의 경우 다시 요청을 보내면 NullPointerException 이 발생하며 해당 회원의 계정삭제가 실패합니다.")
    public void userDeleteAccountTestFailWithDuplicateAccountDeleteRequest() {
        String userId = testUser.getUserId();
        String password = testUser.getPassword();

        when(userRepository.getUserPasswordById(userId)).thenReturn(testUser.getPassword());
        when(passwordEncoder.isMatch(password, testUser.getPassword())).thenReturn(true);

        doNothing().when(userRepository).deleteUser(userId);

        userService.userDeleteAccount(userId, password);

        when(userRepository.getUserPasswordById(userId)).thenReturn(testUser.getPassword());
        when(passwordEncoder.isMatch(password, testUser.getPassword())).thenReturn(true);
        doThrow(NullPointerException.class).when(userRepository).deleteUser(userId);

        assertThrows(NullPointerException.class, () -> {
            userService.userDeleteAccount(userId, password);
        });

        verify(userRepository, times(2)).getUserPasswordById(userId);
        verify(passwordEncoder,times(2)).isMatch(password, testUser.getPassword());
        verify(userRepository, times(2)).deleteUser(userId);
    }
}
