package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.model.User;
import me.soo.helloworld.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

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
}
