package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.exception.IncorrectUserInfoException;
import me.soo.helloworld.model.user.User;
import me.soo.helloworld.model.user.UserLoginRequest;
import me.soo.helloworld.model.user.UserPasswordRequest;
import me.soo.helloworld.repository.UserRepository;
import me.soo.helloworld.util.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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


}
