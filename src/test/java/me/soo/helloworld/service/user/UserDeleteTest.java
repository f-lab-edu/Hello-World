package me.soo.helloworld.service.user;

import me.soo.helloworld.exception.InvalidUserInfoException;
import me.soo.helloworld.mapper.UserMapper;
import me.soo.helloworld.service.EmailService;
import me.soo.helloworld.service.FileService;
import me.soo.helloworld.service.UserService;
import me.soo.helloworld.util.encoder.PasswordEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;

import static me.soo.helloworld.TestUsersFixture.CURRENT_USER;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDeleteTest {

    @InjectMocks
    UserService userService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserMapper userMapper;

    @Mock
    FileService fileService;

    @Mock
    EmailService emailService;

    @Mock
    MockHttpSession httpSession;

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
    @Test
    @DisplayName("요청받은 비밀번호가 현재 사용자의 비밀번호와 일치하면 해당 회원의 계정을 삭제하고 로그아웃 시키는데 성공합니다.")
    public void userDeleteAccountTestSuccess() {
        String userId = CURRENT_USER.getUserId();
        String password = CURRENT_USER.getPassword();

        when(userMapper.getUserPasswordById(userId)).thenReturn(CURRENT_USER.getPassword());
        when(passwordEncoder.isMatch(password, CURRENT_USER.getPassword())).thenReturn(true);

        doNothing().when(userMapper).deleteUser(userId);

        userService.deleteMyAccount(userId, password);

        verify(userMapper, times(1)).getUserPasswordById(userId);
        verify(passwordEncoder,times(1)).isMatch(password, CURRENT_USER.getPassword());
        verify(userMapper, times(1)).deleteUser(userId);
    }

    @Test
    @DisplayName("요청받은 비밀번호가 현재 사용자의 비밀번호와 일치하지 않으면 InvalidUserInfoException 이 발생하며 해당 회원의 계정삭제가 실패합니다.")
    public void userDeleteAccountTestFailWithWrongPassword() {
        String userId = CURRENT_USER.getUserId();
        String password = "I'm gonna take my horse to the old town road~";

        when(userMapper.getUserPasswordById(userId)).thenReturn(CURRENT_USER.getPassword());
        when(passwordEncoder.isMatch(password, CURRENT_USER.getPassword())).thenReturn(false);

        assertThrows(InvalidUserInfoException.class, () -> {
            userService.deleteMyAccount(userId, password);
        });

        verify(userMapper, times(1)).getUserPasswordById(userId);
        verify(passwordEncoder,times(1)).isMatch(password, CURRENT_USER.getPassword());
    }

    @Test
    @DisplayName("이미 삭제 처리된 사용자의 경우 다시 요청을 보내면 NullPointerException 이 발생하며 해당 회원의 계정삭제가 실패합니다.")
    public void userDeleteAccountTestFailWithDuplicateAccountDeleteRequest() {
        String userId = CURRENT_USER.getUserId();
        String password = CURRENT_USER.getPassword();

        when(userMapper.getUserPasswordById(userId)).thenReturn(CURRENT_USER.getPassword());
        when(passwordEncoder.isMatch(password, CURRENT_USER.getPassword())).thenReturn(true);

        doNothing().when(userMapper).deleteUser(userId);

        userService.deleteMyAccount(userId, password);

        when(userMapper.getUserPasswordById(userId)).thenReturn(CURRENT_USER.getPassword());
        when(passwordEncoder.isMatch(password, CURRENT_USER.getPassword())).thenReturn(true);
        doThrow(NullPointerException.class).when(userMapper).deleteUser(userId);

        assertThrows(NullPointerException.class, () -> {
            userService.deleteMyAccount(userId, password);
        });

        verify(userMapper, times(2)).getUserPasswordById(userId);
        verify(passwordEncoder,times(2)).isMatch(password, CURRENT_USER.getPassword());
        verify(userMapper, times(2)).deleteUser(userId);
    }
}
