package me.soo.helloworld.service;

import me.soo.helloworld.model.user.User;
import me.soo.helloworld.model.user.UserPasswordRequest;
import me.soo.helloworld.repository.UserRepository;
import me.soo.helloworld.util.encoder.PasswordEncoder;
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
public class UserUpdateTest {

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
    @DisplayName("현재 유저의 비밀번호가 성공적으로 업데이트 됩니다.")
    public void userPasswordUpdateSuccess() {

        String differentPassword = "Do you wanna build a snow man?";

        UserPasswordRequest newPassword = UserPasswordRequest.builder()
                .currentPassword(testUser.getPassword())
                .newPassword(differentPassword)
                .checkNewPassword(differentPassword)
                .build();

        String encodedPassword = passwordEncoder.encode(newPassword.getNewPassword());

        doNothing().when(userRepository).updateUserPassword(testUser.getUserId(), encodedPassword);

        userService.userPasswordUpdate(testUser.getUserId(), newPassword);

        verify(userRepository, times(1)).updateUserPassword(testUser.getUserId(), encodedPassword);
    }
}

