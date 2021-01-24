package me.soo.helloworld.mapper;

import me.soo.helloworld.model.user.User;
import me.soo.helloworld.model.user.UserLoginInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserMapperTest {
    User testUser;
    UserLoginInfo userLoginInfo1;
    UserLoginInfo userLoginInfo2;

    @Autowired
    UserMapper userMapper;

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
        userLoginInfo1 = new UserLoginInfo(testUser.getUserId(), testUser.getPassword());
        userLoginInfo2 = new UserLoginInfo("msugo1045", "Gomsu1045!0$%");
    }

    @Test
    public void registeredUserCheckTest() {
        userMapper.insertUser(testUser);
        assertTrue(userMapper.isRegisteredUser(userLoginInfo1));
        assertFalse(userMapper.isRegisteredUser(userLoginInfo2));
    }
}