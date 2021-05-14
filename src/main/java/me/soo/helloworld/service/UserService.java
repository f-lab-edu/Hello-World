package me.soo.helloworld.service;

import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import me.soo.helloworld.exception.InvalidUserInfoException;
import me.soo.helloworld.mapper.UserMapper;
import me.soo.helloworld.model.email.EmailBase;
import me.soo.helloworld.model.email.FindPasswordEmail;
import me.soo.helloworld.model.file.FileData;
import me.soo.helloworld.model.user.*;
import me.soo.helloworld.util.encoder.PasswordEncoder;
import me.soo.helloworld.util.encoder.PasswordEncoderSHA256;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static me.soo.helloworld.util.constant.CacheNames.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    private final FileService fileService;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    @CacheEvict(key = MAIN_PAGE_KEY,value = MAIN_PAGE_VALUE, cacheManager = REDIS_CACHE_MANAGER)
    public void userSignUp(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        userMapper.insertUser(user.buildUserWithEncodedPassword(encodedPassword));
    }

    @CacheEvict(key = MAIN_PAGE_KEY,value = MAIN_PAGE_VALUE, cacheManager = REDIS_CACHE_MANAGER)
    public void testUserSignUp(User user) {
        String encodedPassword = PasswordEncoderSHA256.encode(user.getPassword());
        userMapper.insertUser(user.buildUserWithEncodedPassword(encodedPassword));
    }

    @Transactional(readOnly = true)
    public boolean isUserIdExist(String userId) {
        return userMapper.isUserIdExist(userId);
    }

    @Transactional(readOnly = true)
    public UserLoginData getUserLoginInfo(String userId, String password) {
        UserLoginData loginData = userMapper.getUserLoginDataById(userId)
                                .orElseThrow(() -> new InvalidUserInfoException("해당 사용자는 존재하지 않습니다. 아이디를 다시 확인해 주세요."));

        if (!passwordEncoder.isMatch(password, loginData.getPassword())) {
            throw new InvalidUserInfoException("입력하신 비밀번호가 일치하지 않습니다. 다시 한 번 확인해주세요.");
        }
//        isValidPassword(password, loginData.getPassword());
        return loginData;
    }

    @Transactional(readOnly = true)
    public UserLoginData testGetUserLoginInfo(String userId, String password) {
        UserLoginData loginData = userMapper.getUserLoginDataById(userId)
                .orElseThrow(() -> new InvalidUserInfoException("해당 사용자는 존재하지 않습니다. 아이디를 다시 확인해 주세요."));

        if (!StringUtils.equals(PasswordEncoderSHA256.encode(password), loginData.getPassword())) {
            throw new InvalidUserInfoException("입력하신 비밀번호가 일치하지 않습니다. 다시 한 번 확인해주세요.");
        }
//        isValidPassword(password, loginData.getPassword());
        return loginData;
    }

    public void userPasswordUpdate(String userid, UserPasswordRequest userPasswordRequest) {
        String encodedPassword = passwordEncoder.encode(userPasswordRequest.getNewPassword());
        userMapper.updateUserPassword(userid, encodedPassword);
    }

    @CacheEvict(key = "#userId", value = USER_PROFILE, cacheManager = REDIS_CACHE_MANAGER)
    public void userInfoUpdate(String userId, UserUpdateRequest updateRequest) {
        userMapper.updateUserInfo(userId, updateRequest);
    }

    @CacheEvict(key = "#userId", value = USER_PROFILE, cacheManager = REDIS_CACHE_MANAGER)
    public void userProfileImageUpdate(String userId, MultipartFile profileImage) {
        FileData oldProfileImage = userMapper.getUserProfileImageById(userId);

        if (oldProfileImage != null) {
            fileService.deleteFile(oldProfileImage);
        }

        FileData newProfileImage = fileService.uploadFile(profileImage, userId);
        userMapper.updateUserProfileImage(userId, newProfileImage);
    }

    public void findUserPassword(UserFindPasswordRequest findPasswordRequest) {
        if (!userMapper.isEmailValid(findPasswordRequest)) {
            throw new InvalidUserInfoException("해당 사용자가 존재하지 않거나 이메일이 일치하지 않습니다. 입력하신 정보를 다시 확인해 주세요.");
        }

        String temporaryPassword = UUID.randomUUID().toString();
        EmailBase email = FindPasswordEmail.create(findPasswordRequest.getEmail(), temporaryPassword);
        emailService.sendEmail(email);

        userMapper.updateUserPassword(findPasswordRequest.getUserId(), passwordEncoder.encode(temporaryPassword));
    }

    /*
        계정 삭제 요청을 받으면 해당 사용자의 정보를 바로 삭제하는 것이 아니라 일정기간 동안 보관했다가 삭제한다고 하여 물리삭제가 아니라
        계정 비활성화 정보를 'Y'로 바꾸는 식으로 하여 논리삭제를 구현하였습니다.
     */
    public void userDeleteAccount(String userId, String requestPassword) {
        String userPassword = userMapper.getUserPasswordById(userId);
        isValidPassword(requestPassword, userPassword);

        userMapper.deleteUser(userId);
    }

    @Transactional(readOnly = true)
    public boolean isUserActivated(String userId) {
        return userMapper.isUserActivated(userId);
    }

    private void isValidPassword(String requestPassword, String userPassword) {
        if (!passwordEncoder.isMatch(requestPassword, userPassword)) {
            throw new InvalidUserInfoException("입력하신 비밀번호가 일치하지 않습니다. 다시 한 번 확인해주세요.");
        }
    }
}
