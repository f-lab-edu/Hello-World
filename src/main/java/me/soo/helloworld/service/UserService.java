package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.exception.InvalidEmailException;
import me.soo.helloworld.exception.InvalidUserException;
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
            throw new InvalidUserException("해당 사용자는 존재하지 않습니다. 아이디를 다시 확인해 주세요.");
        }

        boolean isMatchingPassword = passwordEncoder.isMatch(requestPassword, user.getPassword());

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

    // 문제점: 메일을 보내는 시간이 너무 오래걸림 - 메일서버 자체의 문제일까? 아니면 로직 자체의 문제일까?
    public void userFindPassword(UserFindPasswordRequest findPasswordRequest) {
        User user = userRepository.getUserById(findPasswordRequest.getUserId());

        // 예외 체크 중복 없애보자
        if (user == null) {
            throw new InvalidUserException("해당 사용자는 존재하지 않습니다. 아이디를 다시 확인해 주세요.");
        }

        if (!user.getEmail().equals(findPasswordRequest.getEmail())) {
            throw new InvalidEmailException("입력하신 이메일이 등록된 이메일과 일치하지 않습니다. 이메일을 다시 확인해주세요.");
        }

        // 비밀번호를 어떻게 더 간결하게 전달해 줄 것인가
        // 비밀번호를 바꿀지라도 인코딩해서 전달은 필수다. why? 로그인 메소드에서 인코딩 됀 비밀번호의 일치를 확인하므로
        String newPassword = UUID.randomUUID().toString();
        emailService.sendEmailWithNewPassword(findPasswordRequest.getEmail(), newPassword);
        userRepository.updateUserPassword(
                findPasswordRequest.getUserId(),
                passwordEncoder.encode(newPassword)
        );
    }

}
