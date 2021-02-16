package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.exception.InvalidUserInfoException;
import me.soo.helloworld.model.email.EmailBase;
import me.soo.helloworld.util.EmailBuilder;
import me.soo.helloworld.model.file.FileData;
import me.soo.helloworld.model.user.*;
import me.soo.helloworld.repository.UserRepository;
import me.soo.helloworld.util.encoder.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final FileService fileService;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    public void userSignUp(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        userRepository.insertUser(user.buildUserWithEncodedPassword(encodedPassword));
    }

    public boolean isUserIdDuplicate(String userId) {
        return userRepository.isUserIdDuplicate(userId);
    }

    public User getUser(String requestId, String requestPassword) {
        User user = userRepository.getUserById(requestId);

        if (user == null) {
            throw new InvalidUserInfoException("해당 사용자는 존재하지 않습니다. 아이디를 다시 확인해 주세요.");
        }

        boolean isMatchingPassword = passwordEncoder.isMatch(requestPassword, user.getPassword());

        if (!isMatchingPassword) {
            throw new InvalidUserInfoException("입력하신 비밀번호가 일치하지 않습니다. 비밀번호를 다시 확인해주세요.");
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

    public void findUserPassword(UserFindPasswordRequest findPasswordRequest) {
        User user = userRepository.getUserById(findPasswordRequest.getUserId());

        if (user == null || !user.getEmail().equals(findPasswordRequest.getEmail())) {
            throw new InvalidUserInfoException("해당 사용자가 존재하지 않거나 이메일이 일치하지 않습니다. 입력하신 정보를 다시 확인해 주세요.");
        }

        String newPassword = UUID.randomUUID().toString();
        String title = "임시 비밀번호 안내입니다.";
        String content = String.format("회원님의 임시 비밀번호는 %s 입니다. 로그인 후 비밀번호를 변경해주세요", newPassword);

        EmailBase email = EmailBuilder.build(user.getEmail(), title, content);
        emailService.sendEmail(email);

        userRepository.updateUserPassword(findPasswordRequest.getUserId(), passwordEncoder.encode(newPassword));

    }

}
