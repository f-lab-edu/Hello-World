package me.soo.helloworld.repository;

import me.soo.helloworld.mapper.UserMapper;
import me.soo.helloworld.model.user.User;
import me.soo.helloworld.model.user.UserIdAndPassword;
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

    UserIdAndPassword correctUserIdAndPassword;

    UserIdAndPassword wrongUserIdWithCorrectPassword;

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

        correctUserIdAndPassword = new UserIdAndPassword(testUser.getUserId(), testUser.getPassword());

        wrongUserIdWithCorrectPassword = new UserIdAndPassword("WrongID", testUser.getPassword());
    }


    @Test
    @DisplayName("DB에 존재하는 사용자의 이름을 호출하면 해당하는 로그인 정보를 호출하는데 성공합니다.")
    public void getRegisteredUserTestSuccess() {
        userMapper.insertUser(testUser);

        UserIdAndPassword registeredUserIdAndPassword = userMapper.getRegisteredUserById(correctUserIdAndPassword.getUserId());
        assertEquals(testUser.getUserId(), registeredUserIdAndPassword.getUserId());
        assertEquals(testUser.getPassword(), registeredUserIdAndPassword.getPassword());

    }

    @Test
    @DisplayName("DB에 존재하지 않는 사용자의 이름을 호출하면 Null을 리턴합니다.")
    public void getRegisteredUserTestFailWithDifferentName() {
        userMapper.insertUser(testUser);

        UserIdAndPassword registeredUserInfo = userMapper.getRegisteredUserById(wrongUserIdWithCorrectPassword.getUserId());
        assertNull(registeredUserInfo);
    }
}