package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.enums.UserExceptionCode;
import me.soo.helloworld.exception.UserException;
import me.soo.helloworld.model.file.FileData;
import me.soo.helloworld.model.user.*;
import me.soo.helloworld.repository.UserRepository;
import me.soo.helloworld.util.encoder.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final FileService fileService;

    private final PasswordEncoder passwordEncoder;

    private final LoginService loginService;

    public void userSignUp(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        userRepository.insertUser(user.buildUserWithEncodedPassword(encodedPassword));
    }

    public boolean isUserIdDuplicate(String userId) {
        return userRepository.isUserIdDuplicate(userId);
    }

    public User getUser(UserLoginRequest loginRequest) {
        User user = userRepository.getUserById(loginRequest.getUserId());

        if (user == null) {
            throw new UserException(UserExceptionCode.INVALID_USER_ID);
        }

        boolean isMatchingPassword = passwordEncoder.isMatch(loginRequest.getPassword(), user.getPassword());

        if (!isMatchingPassword) {
            throw new UserException(UserExceptionCode.INVALID_USER_PASSWORD);
        }

        return user;
    }

    public void userPasswordUpdate(String currentUserId, UserPasswordRequest userPasswordRequest) {
        String encodedPassword = passwordEncoder.encode(userPasswordRequest.getNewPassword());
        userRepository.updateUserPassword(currentUserId, encodedPassword);
        loginService.logout();
    }

    public void userInfoUpdate(String userId, UserUpdateRequest updateRequest) {
        userRepository.updateUserInfo(userId, updateRequest);
    }

    public void userProfileImageUpdate(String userId, MultipartFile profileImage) {

            FileData oldProfileImage = userRepository.getUserProfileImageById(userId);

            if (oldProfileImage != null) {
                fileService.deleteFile(oldProfileImage);
            }

            FileData newProfileImage = fileService.uploadFile(profileImage, userId);
            userRepository.updateUserProfileImage(userId, newProfileImage);
    }
}
