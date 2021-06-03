package me.soo.helloworld.service.user;

import me.soo.helloworld.exception.file.FileNotDeletedException;
import me.soo.helloworld.exception.file.FileNotUploadedException;
import me.soo.helloworld.mapper.UserMapper;
import me.soo.helloworld.model.file.FileData;
import me.soo.helloworld.model.user.UpdatePasswordRequest;
import me.soo.helloworld.model.user.UpdateInfoRequest;
import me.soo.helloworld.service.EmailService;
import me.soo.helloworld.service.FileService;
import me.soo.helloworld.service.LoginService;
import me.soo.helloworld.service.UserService;
import me.soo.helloworld.util.encoder.PasswordEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import static me.soo.helloworld.TestCountries.*;
import static me.soo.helloworld.TestUsersFixture.CURRENT_USER;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserUpdateTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserMapper userMapper;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    FileService fileService;

    @Mock
    EmailService emailService;

    @Mock
    LoginService loginService;

    @Test
    @DisplayName("현재 유저의 비밀번호가 성공적으로 업데이트 됩니다.")
    public void userPasswordUpdateSuccess() {
        String differentPassword = "Do you wanna build a snow man?";
        UpdatePasswordRequest newPassword = new UpdatePasswordRequest(
                CURRENT_USER.getPassword(),
                differentPassword,
                differentPassword
        );
        String encodedPassword = passwordEncoder.encode(newPassword.getNewPassword());

        doNothing().when(userMapper).updateUserPassword(CURRENT_USER.getUserId(), encodedPassword);
        userService.updatePassword(CURRENT_USER.getUserId(), newPassword);

        verify(userMapper, times(1)).updateUserPassword(CURRENT_USER.getUserId(), encodedPassword);
    }

    @Test
    @DisplayName("현재 유저의 회원정보가 성공적으로 업데이트 됩니다.")
    public void userInfoUpdateSuccess() {
        UpdateInfoRequest updatedUser = new UpdateInfoRequest(
                "M",
                UNITED_KINGDOM,
                OTHERS,
                "I have just been accepted to the Hogwart of Witchcraft and Wizardary"
        );

        doNothing().when(userMapper).updateUserInfo(CURRENT_USER.getUserId(), updatedUser);
        userService.updateProfile(CURRENT_USER.getUserId(), updatedUser);

        verify(userMapper, times(1)).updateUserInfo(CURRENT_USER.getUserId(), updatedUser);
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

        FileData newProfileImage = new FileData(newFileName, CURRENT_USER.getProfileImagePath());

        when(fileService.uploadFile(testImageFile, CURRENT_USER.getUserId())).thenReturn(newProfileImage);
        doNothing().when(userMapper).updateUserProfileImage(CURRENT_USER.getUserId(), newProfileImage);

        userService.updateProfileImage(CURRENT_USER.getUserId(), testImageFile);

        verify(fileService, times(1)).uploadFile(testImageFile, CURRENT_USER.getUserId());
        verify(userMapper, times(1)).updateUserProfileImage(CURRENT_USER.getUserId(), newProfileImage);

    }

    @Test
    @DisplayName("현재 유저의 기존 프로필 사진 삭제에 실패하면 프로필 사진 업데이트에 실패하며 FileNotDeletedException 이 발생합니다.")
    public void userProfileImageUpdateFailWithExistingFileRemovalNotPossible() {
        FileData oldProfileImage = new FileData(CURRENT_USER.getProfileImageName(), "D:\\Project\\");

        MockMultipartFile testImageFile = new MockMultipartFile(
                "profileImage",
                "profileImage",
                "image/jpeg",
                "Hello There".getBytes());

        when(userMapper.getUserProfileImageById(CURRENT_USER.getUserId())).thenReturn(oldProfileImage);
        doThrow(FileNotDeletedException.class).when(fileService).deleteFile(oldProfileImage);

        assertThrows(FileNotDeletedException.class, () -> {
            userService.updateProfileImage(CURRENT_USER.getUserId(), testImageFile);
        });

        verify(fileService, times(1)).deleteFile(oldProfileImage);
    }

    @Test
    @DisplayName("새로운 프로필 사진의 업로드에 실패하는 경우 프로필 사진 업데이트에 실패하며 FileNotUploadedException 이 발생합니다.")
    public void userProfileImageUpdateFailWithUploadingNotPossible() {
        FileData oldProfileImage = new FileData(CURRENT_USER.getProfileImageName(), CURRENT_USER.getProfileImagePath());

        MockMultipartFile testImageFile = new MockMultipartFile(
                "profileImage",
                "profileImage",
                "image/jpeg",
                "Hello There".getBytes());

        String newFileName = "HermioneGranger.jpg";

        when(userMapper.getUserProfileImageById(CURRENT_USER.getUserId())).thenReturn(oldProfileImage);
        doNothing().when(fileService).deleteFile(oldProfileImage);

        when(fileService.uploadFile(testImageFile, CURRENT_USER.getUserId())).thenThrow(FileNotUploadedException.class);

        assertThrows(FileNotUploadedException.class, () -> {
            userService.updateProfileImage(CURRENT_USER.getUserId(), testImageFile);
        });

        verify(fileService, times(1)).deleteFile(oldProfileImage);
        verify(fileService, times(1)).uploadFile(testImageFile, CURRENT_USER.getUserId());
    }
}

