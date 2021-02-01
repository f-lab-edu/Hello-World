package me.soo.helloworld.service;

import me.soo.helloworld.model.user.User;
import me.soo.helloworld.model.user.UserLoginRequest;
import me.soo.helloworld.model.user.UserPasswordUpdateRequest;
import me.soo.helloworld.repository.UserRepository;
import me.soo.helloworld.util.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class UpdatePasswordTest {

    User testUser;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        String encodedPassword = passwordEncoder.encode("Bakery");

        testUser = new User(
                "Soo",
                encodedPassword,
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
    public void passwordEncoderCheck() {
        String password = "I am found~~ show yourself";

        UserPasswordUpdateRequest newPassword = UserPasswordUpdateRequest.builder()
                .currentPassword("Bakery")
                .checkNewPassword(password)
                .checkNewPassword(password)
                .build();

        assertEquals(newPassword.getCurrentPassword(), testUser.getPassword());

        userService.userPasswordUpdate(newPassword, testUser);
    }

    @Test
    public void userPasswordUpdateSuccess() {
        userRepository.insertUser(testUser);

        String password = "I am found~~ show yourself";

        UserPasswordUpdateRequest newPassword = UserPasswordUpdateRequest.builder()
                .currentPassword("Bakery")
                .newPassword(password)
                .checkNewPassword(password)
                .build();

        userService.userPasswordUpdate(newPassword, testUser);

        User updatedUser = userRepository.getUserById(testUser.getUserId());

        assertNotNull(updatedUser);

        assertEquals(testUser.getUserId(), updatedUser.getUserId());
        assertTrue(passwordEncoder.isMatch(newPassword.getNewPassword(), updatedUser.getPassword()));
        assertNotEquals(testUser.getPassword(), updatedUser.getPassword());
    }



}
