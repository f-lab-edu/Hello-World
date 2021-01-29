package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.model.User;
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

    public boolean isUserIdDuplicate(String userId) {
        return userRepository.isUserIdDuplicate(userId);
    }
}
