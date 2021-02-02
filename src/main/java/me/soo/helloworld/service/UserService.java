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
        String encodedPassword = encodePassword(user.getPassword());
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

        passwordMatchingCheck(loginRequest.getPassword(), user.getPassword());

        return user;
    }

    /*
        코드 중복을 피하기 위해서 refactoring 방법을 혼자 고민하다 더 나은 방법이 떠오르지 않아서 method extract 방법을 일단 선택하였습니다.
        (passwordMatchingCheck)
        중복은 나름 없앴지만 주석 없이는 가독성이 많이 떨어지는 것 같은데 좋은 방법이 없을까요..?
     */
    public void userPasswordUpdate(UserPasswordRequest userPasswordRequest, User currentUser) {
        // 현재 비밀번호가 일치하지 않으면 예외발생
        passwordMatchingCheck(userPasswordRequest.getCurrentPassword(), currentUser.getPassword());

        String encodedNewPassword = passwordEncoder.encode(userPasswordRequest.getNewPassword());
        // 새로운 비밀번호와 확인 값이 일치하지 않으면 예외발생
        passwordMatchingCheck(userPasswordRequest.getCheckNewPassword(), encodedNewPassword);

        userRepository.updateUserPassword(currentUser.getUserId(), encodedNewPassword);
    }

    private String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    private void passwordMatchingCheck(String password, String passwordToCompare) {
        boolean isMatchingPassword = passwordEncoder.isMatch(password, passwordToCompare);

        if (!isMatchingPassword) {
            throw new IncorrectUserInfoException("입력하신 비밀번호가 일치하지 않습니다. 비밀번호를 다시 한 번 확인해주세요.");
        }
    }

}
