package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.exception.IncorrectUserInfoException;
import me.soo.helloworld.model.file.FileData;
import me.soo.helloworld.model.user.*;
import me.soo.helloworld.repository.UserRepository;
import me.soo.helloworld.util.encoder.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
            throw new IncorrectUserInfoException("해당 유저의 정보는 존재하지 않습니다. 아이디를 다시 확인해주세요.");
        }

        boolean isMatchingPassword = passwordEncoder.isMatch(loginRequest.getPassword(), user.getPassword());

        if (!isMatchingPassword) {
            throw new IncorrectUserInfoException("입력하신 비밀번호가 일치하지 않습니다. 비밀번호를 다시 한 번 확인해주세요.");
        }

        return user;
    }

    public void userPasswordUpdate(String currentUserId, UserPasswordRequest userPasswordRequest) {
        String encodedPassword = passwordEncoder.encode(userPasswordRequest.getNewPassword());
        userRepository.updateUserPassword(currentUserId, encodedPassword);
    }

    public UserUpdate userInfoUpdate(String userId, MultipartFile profileImage, UserUpdateRequest updateRequest) throws IOException {

        /*
        * 할일
        * 1. 컨트롤러 손보기
        * 2. 서비스 손보기
        * - 여기: 파일 경로/이름 얻는법 분리하기 (파일서비스로)
        * - 여기: updateUser builder 메소드 책임 이관 -> user update 로 like user
        *
        * - 파일서비스: 파일 경로 얻는법 알아내기
        *
        *  파일경로만 얻어서 리턴할 수 있으면 되지 않을까?
        *
        * 4. 테스트 작성
        *         * */

        // 기존에 있는 파일은 어떻게 지울까?

        FileData profileImageFileData = fileService.uploadFile(profileImage, userId);
        UserUpdate updatedUser = UserUpdate.buildUpdatedUser(userId, updateRequest, profileImageFileData);

        userRepository.updateUser(updatedUser);
        return updatedUser;
    }


}
