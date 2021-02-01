package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.exception.IncorrectUserInfoException;
import me.soo.helloworld.model.user.User;
import me.soo.helloworld.model.user.UserLoginRequest;
import me.soo.helloworld.model.user.UserPasswordUpdateRequest;
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

        // 책임 분리 필요 - 어떻게 해야할까?
        if (user == null) {
            throw new IncorrectUserInfoException("해당 유저의 정보는 존재하지 않습니다. 아이디를 다시 확인해주세요.");
        }

        boolean isMatchingPassword = passwordEncoder.isMatch(loginRequest.getPassword(), user.getPassword());

        if (!isMatchingPassword) {
            throw new IncorrectUserInfoException("비밀번호를 다시 한 번 확인해주세요.");
        }

        return user;
    }

    /**
     * 효율적인 리팩토링 방법 생각해보기
     * 1. 메소드 추출
     * 2. 템플릿/콜백?
     * 3. AOP 여기에 쓰이나? 혹은 a bit too much 인가?
     */
    public void userPasswordUpdate(UserPasswordUpdateRequest newPassword, User currentUser) {
        boolean isMatchingPassword = passwordEncoder.isMatch(
                newPassword.getCurrentPassword(), currentUser.getPassword()
        );

        if (!isMatchingPassword) {
            throw new IncorrectUserInfoException("비밀번호를 다시 한 번 확인해주세요. 1");
        }

        String encodedPassword = passwordEncoder.encode(newPassword.getNewPassword());

        boolean isPasswordSame = passwordEncoder.isMatch(newPassword.getCheckNewPassword(), encodedPassword);

        if (!isPasswordSame) {
            throw new IncorrectUserInfoException("비밀번호를 다시 한 번 확인해주세요. 2");
        }

        userRepository.updateUserPassword(currentUser.getUserId(), encodedPassword);

    }
}
