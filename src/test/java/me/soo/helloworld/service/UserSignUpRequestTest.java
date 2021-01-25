package me.soo.helloworld.service;

import com.sun.jdi.request.DuplicateRequestException;
import me.soo.helloworld.model.user.User;
import me.soo.helloworld.model.user.UserLoginInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.http.HttpSession;
import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserSignUpRequestTest {
    User testUser;

    @Autowired
    UserService userService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    HttpSession httpSession;

    @BeforeEach
    public void createUsers() {
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
    public void loginUserServiceTest() {
        userService.insertUser(testUser);
    }


    @Test
    public void duplicateUserIdException() {
        userService.isUserIdDuplicate(testUser.getUserId());
    }
}