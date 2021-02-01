package me.soo.helloworld.service;

import me.soo.helloworld.exception.IncorrectUserInfoException;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserInfoUpdateTest {

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
    @DisplayName("기존 비밀번호에 대한 인증이 끝난 후 현재 비밀번호와 다른 새로운 비밀번호를 입력하여 그 입력값이 확인값과 같은 경우 " +
            "현재 유저의 비밀번호가 성공적으로 업데이트 됩니다.")
    public void userPasswordUpdateSuccess() {

        String differentPassword = "Do you wanna build a snow man?";

        UserPasswordUpdateRequest newPassword = UserPasswordUpdateRequest.builder()
                .currentPassword(testUser.getPassword())
                .newPassword(differentPassword)
                .checkNewPassword(differentPassword)
                .build();

        String encodedPassword = passwordEncoder.encode(newPassword.getNewPassword());

        when(passwordEncoder.isMatch(newPassword.getCurrentPassword(), testUser.getPassword())).thenReturn(true);
        when(passwordEncoder.isMatch(newPassword.getCheckNewPassword(), encodedPassword)).thenReturn(true);
        doNothing().when(userRepository).updateUserPassword(testUser.getUserId(), encodedPassword);

        userService.userPasswordUpdate(newPassword, testUser);

        verify(passwordEncoder, times(1))
                .isMatch(newPassword.getCurrentPassword(), testUser.getPassword());
        verify(passwordEncoder, times(1))
                .isMatch(newPassword.getCheckNewPassword(), encodedPassword);
        verify(userRepository, times(1)).updateUserPassword(testUser.getUserId(), encodedPassword);
    }

    @Test
    @DisplayName("현재 사용자의 기존 비밀번호 인증이 실패한 경우 비밀번호 업데이트는 실패하며 IncorrectUserInfoException이 발생합니다.")
    public void userPasswordUpdateFailNotMatchingCurrent() {

        String differentPassword = "Do you wanna build a snow man?";

        UserPasswordUpdateRequest newPassword = UserPasswordUpdateRequest.builder()
                .currentPassword(differentPassword)
                .newPassword(differentPassword)
                .checkNewPassword(differentPassword)
                .build();

        when(passwordEncoder.isMatch(newPassword.getCurrentPassword(), testUser.getPassword())).thenReturn(false);

        assertThrows(IncorrectUserInfoException.class, () -> {
            userService.userPasswordUpdate(newPassword, testUser);
        });

        verify(passwordEncoder, times(1))
                .isMatch(newPassword.getCurrentPassword(), testUser.getPassword());
    }

    @Test
    @DisplayName("현재 사용자의 기존 비밀번호 인증이 성공했더라도 새로운 비밀번호와 그에 대한 확인 값이 다르면 " +
            "업데이트는 실패하며 IncorrectUserInfoException이 발생합니다.")
    public void userPasswordUpdateFailMatchingWithCurrentButNotCheckNew() {

        String differentPassword = "Do you wanna build a snow man?";
        String distinctPassword = "Show yourself! Let me see who you are";

        UserPasswordUpdateRequest newPassword = UserPasswordUpdateRequest.builder()
                .currentPassword(testUser.getPassword())
                .newPassword(differentPassword)
                .checkNewPassword(distinctPassword)
                .build();

        when(passwordEncoder.isMatch(newPassword.getCurrentPassword(), testUser.getPassword())).thenReturn(true);
        String encodedNewPassword = passwordEncoder.encode(newPassword.getNewPassword());
        when(passwordEncoder.isMatch(newPassword.getCheckNewPassword(), encodedNewPassword)).thenReturn(false);

        assertThrows(IncorrectUserInfoException.class, () -> {
            userService.userPasswordUpdate(newPassword, testUser);
        });

        verify(passwordEncoder, times(1))
                .isMatch(newPassword.getCurrentPassword(), testUser.getPassword());
        verify(passwordEncoder, times(1))
                .isMatch(newPassword.getCheckNewPassword(), encodedNewPassword);
    }
}

