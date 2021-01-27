package me.soo.helloworld.service;

import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import me.soo.helloworld.model.user.User;
import me.soo.helloworld.model.user.UserLoginInfo;
import me.soo.helloworld.repository.UserRepository;
import me.soo.helloworld.util.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void userSignUp(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        User encodedUser = User.builder()
                .userId(user.getUserId())
                .password(encodedPassword)
                .email(user.getEmail())
                .gender(user.getGender())
                .birthday(user.getBirthday())
                .originCountry(user.getOriginCountry())
                .livingCountry(user.getLivingCountry())
                .livingTown(user.getLivingTown())
                .aboutMe(user.getAboutMe())
                .build();

        userRepository.insertUser(encodedUser);
    }

    @Override
    public boolean isUserIdDuplicate(String userId) {
        return userRepository.isUserIdDuplicate(userId);
    }

    @Override
    public void loginRequest(UserLoginInfo userLoginInfo, HttpSession httpSession) {
        if (httpSession.getAttribute("userId") != null) {
            throw new DuplicateRequestException("해당 유저는 이미 로그인 되어 있습니다.");
        }

        if (!userRepository.isRegisteredUser(userLoginInfo)) {
            throw new IllegalArgumentException("해당 유저는 존재하지 않습니다.");
        }

        httpSession.setAttribute("userId", userLoginInfo.getUserId());
    }

    @Override
    public void logoutRequest(HttpSession httpSession) {
        httpSession.invalidate();
    }
}
