package me.soo.helloworld.repository;

import me.soo.helloworld.mapper.UserMapper;
import me.soo.helloworld.model.user.User;
import me.soo.helloworld.model.user.UserLoginInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {
    User testUser;
    UserLoginInfo testUserLoginInfo;
    UserLoginInfo wrongIdUserLoginInfo;

    @Autowired
    UserMapper userMapper;

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
        testUserLoginInfo = new UserLoginInfo("gomsu1045", "Gomsu1045!0$%");
        wrongIdUserLoginInfo = new UserLoginInfo("Soo", "Gomsu1045!0$%");
    }


    @Test
    @DisplayName("DB에 존재하는 사용자의 이름을 호출하면 해당하는 로그인 정보를 호출하는데 성공합니다.")
    public void getRegisteredUserTestSuccess() {
        userMapper.insertUser(testUser);

        UserLoginInfo registeredUserInfo = userMapper.getRegisteredUserInfo(testUserLoginInfo);
        assertEquals(registeredUserInfo, testUserLoginInfo);
    }

    @Test
    @DisplayName("DB에 존재하지 않는 사용자의 이름을 호출하면 Null을 리턴합니다.")
    public void getRegisteredUserTestFailWithDifferentName() {
        userMapper.insertUser(testUser);

        UserLoginInfo registeredUserInfo = userMapper.getRegisteredUserInfo(wrongIdUserLoginInfo);
        assertNotEquals(registeredUserInfo, wrongIdUserLoginInfo);
        assertNull(registeredUserInfo);
    }
}