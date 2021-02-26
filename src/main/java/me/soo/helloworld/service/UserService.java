package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.exception.InvalidUserInfoException;
import me.soo.helloworld.model.email.EmailBase;
import me.soo.helloworld.model.email.FindPasswordEmail;
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

        isValidPassword(requestPassword, user.getPassword());

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

        String temporaryPassword = UUID.randomUUID().toString();
        EmailBase email = FindPasswordEmail.create(findPasswordRequest.getEmail(), temporaryPassword);
        emailService.sendEmail(email);

        userRepository.updateUserPassword(findPasswordRequest.getUserId(), passwordEncoder.encode(temporaryPassword));
    }

    /*
        계정 삭제 요청을 받으면 해당 사용자의 정보를 바로 삭제하는 것이 아니라 일정기간 동안 보관했다가 삭제한다고 하여 물리삭제가 아니라
        계정 비활성화 정보를 'Y'로 바꾸는 식으로 하여 논리삭제를 구현하였습니다.
     */
    public void userDeleteAccount(String userId, String requestPassword) {
        String userPassword = userRepository.getUserPasswordById(userId);

        isValidPassword(requestPassword, userPassword);

        userRepository.deleteUser(userId);
    }

    private void isValidPassword(String requestPassword, String userPassword) {
        if (!passwordEncoder.isMatch(requestPassword, userPassword)) {
            throw new InvalidUserInfoException("입력하신 비밀번호가 일치하지 않습니다. 다시 한 번 확인해주세요.");
        }
    }
}
