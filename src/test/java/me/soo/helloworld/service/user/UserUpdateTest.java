package me.soo.helloworld.service.user;

import me.soo.helloworld.exception.file.FileNotDeletedException;
import me.soo.helloworld.exception.file.FileNotUploadedException;
import me.soo.helloworld.mapper.UserMapper;
import me.soo.helloworld.model.file.FileData;
import me.soo.helloworld.model.user.User;
import me.soo.helloworld.model.user.UserPasswordRequest;
import me.soo.helloworld.model.user.UserUpdateRequest;
import me.soo.helloworld.service.FileService;
import me.soo.helloworld.service.LoginService;
import me.soo.helloworld.service.UserService;
import me.soo.helloworld.util.encoder.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.sql.Date;

import static me.soo.helloworld.TestCountries.SOUTH_KOREA;
import static me.soo.helloworld.TestCountries.UNITED_KINGDOM;
import static me.soo.helloworld.TestTowns.NEWCASTLE;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserUpdateTest {

    User testUser;

    @InjectMocks
    UserService userService;

    @Mock
    UserMapper userMapper;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    FileService fileService;

    @Mock
    LoginService loginService;

    @BeforeEach
    public void setUp() {

        String filePath = "D:\\Project\\Hello-World\\Files";

        String fileName = "IsItSuccessful.jpg";

        testUser = User.builder()
                .userId("gomsu1045")
                .password("Gomsu1045!0$%")
                .email("test@test.com")
                .gender("Male")
                .birthday(Date.valueOf("1993-09-25"))
                .originCountry(SOUTH_KOREA)
                .livingCountry(UNITED_KINGDOM)
                .livingTown(NEWCASTLE)
                .aboutMe("Hello, I'd love to make great friends here")
                .profileImageName(fileName)
                .profileImagePath(filePath)
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

        doNothing().when(userMapper).updateUserPassword(testUser.getUserId(), encodedPassword);

        userService.userPasswordUpdate(testUser.getUserId(), newPassword);

        verify(userMapper, times(1)).updateUserPassword(testUser.getUserId(), encodedPassword);
    }

    @Test
    @DisplayName("현재 유저의 회원정보가 성공적으로 업데이트 됩니다.")
    public void userInfoUpdateSuccess() {

        UserUpdateRequest updatedUser = UserUpdateRequest.builder()
                .gender("M")
                .livingCountry("Wizarding World")
                .livingTown("Hogwart")
                .aboutMe("I have just been accepted to the Hogwart of Witchcraft and Wizardary")
                .build();

        doNothing().when(userMapper).updateUserInfo(testUser.getUserId(), updatedUser);

        userService.userInfoUpdate(testUser.getUserId(), updatedUser);

        verify(userMapper, times(1)).updateUserInfo(testUser.getUserId(), updatedUser);
    }

    @Test
    @DisplayName("현재 유저의 프로필 사진이 성공적으로 업데이트 됩니다.")
    public void userProfileImageUpdateSuccess() {

        MockMultipartFile testImageFile = new MockMultipartFile(
                "profileImage",
                "profileImage",
                "image/jpeg",
                "Hello There".getBytes());

        String newFileName = "HermioneGranger.jpg";

        FileData newProfileImage = new FileData(newFileName, testUser.getProfileImagePath());

        when(fileService.uploadFile(testImageFile, testUser.getUserId())).thenReturn(newProfileImage);
        doNothing().when(userMapper).updateUserProfileImage(testUser.getUserId(), newProfileImage);

        userService.userProfileImageUpdate(testUser.getUserId(), testImageFile);

        verify(fileService, times(1)).uploadFile(testImageFile, testUser.getUserId());
        verify(userMapper, times(1)).updateUserProfileImage(testUser.getUserId(), newProfileImage);

    }

    @Test
    @DisplayName("현재 유저의 기존 프로필 사진 삭제에 실패하면 프로필 사진 업데이트에 실패하며 FileNotDeletedException 이 발생합니다.")
    public void userProfileImageUpdateFailWithExistingFileRemovalNotPossible() {

        FileData oldProfileImage = new FileData(testUser.getProfileImageName(), "D:\\Project\\");

        MockMultipartFile testImageFile = new MockMultipartFile(
                "profileImage",
                "profileImage",
                "image/jpeg",
                "Hello There".getBytes());

        when(userMapper.getUserProfileImageById(testUser.getUserId())).thenReturn(oldProfileImage);
        doThrow(FileNotDeletedException.class).when(fileService).deleteFile(oldProfileImage);

        assertThrows(FileNotDeletedException.class, () -> {
            userService.userProfileImageUpdate(testUser.getUserId(), testImageFile);
        });

        verify(fileService, times(1)).deleteFile(oldProfileImage);
    }

    @Test
    @DisplayName("새로운 프로필 사진의 업로드에 실패하는 경우 프로필 사진 업데이트에 실패하며 FileNotUploadedException 이 발생합니다.")
    public void userProfileImageUpdateFailWithUploadingNotPossible() {

        FileData oldProfileImage = new FileData(testUser.getProfileImageName(), testUser.getProfileImagePath());

        MockMultipartFile testImageFile = new MockMultipartFile(
                "profileImage",
                "profileImage",
                "image/jpeg",
                "Hello There".getBytes());

        String newFileName = "HermioneGranger.jpg";

        when(userMapper.getUserProfileImageById(testUser.getUserId())).thenReturn(oldProfileImage);
        doNothing().when(fileService).deleteFile(oldProfileImage);

        when(fileService.uploadFile(testImageFile, testUser.getUserId())).thenThrow(FileNotUploadedException.class);

        assertThrows(FileNotUploadedException.class, () -> {
            userService.userProfileImageUpdate(testUser.getUserId(), testImageFile);
        });

        verify(fileService, times(1)).deleteFile(oldProfileImage);
        verify(fileService, times(1)).uploadFile(testImageFile, testUser.getUserId());

    }
}

