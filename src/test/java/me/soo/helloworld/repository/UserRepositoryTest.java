package me.soo.helloworld.repository;

import me.soo.helloworld.mapper.UserMapper;
import me.soo.helloworld.model.file.FileData;
import me.soo.helloworld.model.user.User;
import me.soo.helloworld.model.user.UserUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("해당 사용자의 업데이트 정보를 DB 내에 반영합니다.")
    public void userUpdateInfoTestSuccess() {
        userMapper.insertUser(testUser);

        UserUpdateRequest updatedUser = UserUpdateRequest.builder()
                .gender("M")
                .livingCountry("Republic Of Ireland")
                .livingTown("Dublin")
                .aboutMe("I've just moved to Dublin today")
                .build();

        userMapper.updateUserInfo(testUser.getUserId(), updatedUser);

        User userFromDB = userMapper.getUserById(testUser.getUserId());

        assertEquals(testUser.getUserId(), userFromDB.getUserId());
        assertEquals(testUser.getPassword(), userFromDB.getPassword());
        assertEquals(testUser.getEmail(), userFromDB.getEmail());
        assertNotEquals(testUser.getGender(), userFromDB.getGender());
        assertEquals(testUser.getBirthday(), userFromDB.getBirthday());
        assertEquals(testUser.getOriginCountry(), userFromDB.getOriginCountry());
        assertNotEquals(testUser.getLivingCountry(), userFromDB.getLivingCountry());
        assertNotEquals(testUser.getLivingTown(), userFromDB.getLivingTown());
        assertNotEquals(testUser.getAboutMe(), userFromDB.getAboutMe());
    }

    @Test
    @DisplayName("현재 사용자의 프로필 사진 정보를 DB에 추가합니다.")
    public void userUpdateProfileImageTestSuccess() {
        userMapper.insertUser(testUser);

        String dir = "D:\\Project\\Hello-World\\Files";
        String fileName = "IsItSuccessful.txt";
        FileData profileImage = new FileData(fileName, dir);

        userMapper.updateUserProfileImage(testUser.getUserId(), profileImage);

        User userFromDB = userMapper.getUserById(testUser.getUserId());

        assertEquals(profileImage.getFileName(), userFromDB.getProfileImageName());
        assertEquals(profileImage.getFilePath(), userFromDB.getProfileImagePath());
    }

    @Test
    @DisplayName("현재 사용자의 ID를 이용해 DB에서 비밀번호를 가져옵니다")
    public void getUserPasswordByIdTestSuccess() {
        userMapper.insertUser(testUser);

        String passwordFromDB = userMapper.getUserPasswordById(testUser.getUserId());

        assertEquals(testUser.getPassword(), passwordFromDB);
    }

    @Test
    @DisplayName("DB에 존재하지 않는 사용자의 ID가 요청으로 들어오면 DB에서 해당 회원의 비밀번호를 가져오는데 실패하고 Null을 리턴합니다.")
    public void getUserPasswordByIdTestFailWithInvalidId() {
        userMapper.insertUser(testUser);

        String passwordFromDB = userMapper.getUserPasswordById("wrongID");

        assertNotEquals(testUser.getPassword(), passwordFromDB);
        assertNull(passwordFromDB);
    }

    @Test
    @DisplayName("현재 존재하는 회원의 경우 올바른 ID를 입력하면 DB에서 해당 회원에 대한 정보를 삭제합니다.")
    public void deleteUserTestSuccess() {
        userMapper.insertUser(testUser);

        User userFromDB = userMapper.getUserById(testUser.getUserId());
        assertNotNull(userFromDB);

        userMapper.deleteUser(testUser.getUserId());
        User userFromDBAgain = userMapper.getUserById(testUser.getUserId());

        assertNull(userFromDBAgain);
    }

    @Test
    @DisplayName("이미 삭제처리 된 회원의 경우 DB 에서 해당 회원의 ID를 이용해 회원 정보를 가져올 수 없으므로 회원 정보 삭제가 실패합니다.")
    public void deleteUserTestFailWithDuplicateRequest() {
        userMapper.insertUser(testUser);

        User userFromDB = userMapper.getUserById(testUser.getUserId());
        assertNotNull(userFromDB);

        userMapper.deleteUser(testUser.getUserId());

        User userFromDBAgain = userMapper.getUserById(testUser.getUserId());

        assertNull(userFromDBAgain);

        assertThrows(NullPointerException.class, () -> {
            userMapper.deleteUser(userFromDBAgain.getUserId());
        });
    }
}
