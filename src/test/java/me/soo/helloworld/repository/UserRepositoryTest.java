package me.soo.helloworld.repository;

import me.soo.helloworld.mapper.UserMapper;
import me.soo.helloworld.model.user.User;
import me.soo.helloworld.model.user.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class UserRepositoryTest {

    User testUser;

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
    }

    @Test
    @DisplayName("DB에 등록된 회원과 일치하는 ID를 입력하면 해당 사용자의 정보를 담은 객체를 받아옵니다.")
    public void getUserByIdAndPasswordSuccess() {
        userMapper.insertUser(testUser);

        User dbUser = userMapper.getUserById(testUser.getUserId());

        assertEquals(testUser.getUserId(), dbUser.getUserId());
        assertEquals(testUser.getPassword(), dbUser.getPassword());
        assertEquals(testUser.getEmail(), dbUser.getEmail());
        assertEquals(testUser.getGender(), dbUser.getGender());
        assertEquals(testUser.getBirthday(), dbUser.getBirthday());
        assertEquals(testUser.getOriginCountry(), dbUser.getOriginCountry());
        assertEquals(testUser.getLivingCountry(), dbUser.getLivingCountry());
        assertEquals(testUser.getLivingTown(), dbUser.getLivingTown());
        assertEquals(testUser.getAboutMe(), dbUser.getAboutMe());
    }

    @Test
    @DisplayName("DB에 등록되지 않은 사용자의 ID를 입력하면 null을 리턴합니다.")
    public void getUserByIdAndPasswordFailWithWrongId() {
        userMapper.insertUser(testUser);

        User dbUser = userMapper.getUserById("Hello World");

        assertNull(dbUser);
    }

    @Test
    @DisplayName("DB에 등록되어 있는 사용자의 ID가 일치하면 해당 사용자의 비밀번호가 변경 됩니다.")
    public void userPasswordUpdateSuccessWithCorrectId() {
        userMapper.insertUser(testUser);

        String newPassword = "Into the Unknown";

        userMapper.updateUserPassword(testUser.getUserId(), newPassword);

        User newPasswordUser = userMapper.getUserById(testUser.getUserId());

        assertEquals(testUser.getUserId(), newPasswordUser.getUserId());
        assertNotEquals(testUser.getPassword(), newPasswordUser.getPassword());
        assertEquals(newPassword, newPasswordUser.getPassword());
    }

    @Test
    public void userUpdateTest() {
        String dir = "D:\\Project\\Hello-World\\Files";
        String fileName = "soDifficult.txt";
        File file = new File(fileName, dir);

        UserUpdate updatedUSer = UserUpdate.builder()
                .userId(testUser.getUserId())
                .gender("M")
                .livingCountry("Republic Of Ireland")
                .livingTown("Dublin")
                .aboutMe("I've just moved to Dublin today")
                .profileImagePath(dir)
                .profileImageName(fileName)
                .build();

        userMapper.updateUser(updatedUSer);
    }
}
