package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.exception.InvalidUserException;
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
            throw new InvalidUserException("해당 사용자는 존재하지 않습니다. 아이디를 다시 확인해 주세요.");
        }

        boolean isMatchingPassword = passwordEncoder.isMatch(loginRequest.getPassword(), user.getPassword());

        if (!isMatchingPassword) {
            throw new InvalidUserException("입력하신 비밀번호가 일치하지 않습니다. 비밀번호를 다시 확인해주세요.");
        }

        return user;
    }

    public void userPasswordUpdate(String userid, UserPasswordRequest userPasswordRequest) {
        String encodedPassword = passwordEncoder.encode(userPasswordRequest.getNewPassword());
        userRepository.updateUserPassword(userid, encodedPassword);
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
