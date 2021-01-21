package me.soo.helloworld.service;

import me.soo.helloworld.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserServiceImplTest {
    User testUser;

    @Autowired
    UserService userService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

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
    public void insertUserServiceTest() {

        userService.insertUser(testUser);
    }

    @Test
    public void duplicateUserIdException() {
        userService.isUserIdDuplicate(testUser.getUserId());

    }
}