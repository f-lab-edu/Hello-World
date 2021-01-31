package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.model.user.User;
import me.soo.helloworld.model.user.UserIdAndPassword;
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

    public UserIdAndPassword getLoginUser(UserIdAndPassword requestedUserIdAndPassword) {
        return userRepository.getRegisteredUserById(requestedUserIdAndPassword.getUserId());
    }

}
