package me.soo.helloworld.repository;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.mapper.UserMapper;
import me.soo.helloworld.model.user.User;
import me.soo.helloworld.model.user.UserLoginInfo;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final UserMapper userMapper;

    public void insertUser(User user) {
        userMapper.insertUser(user);
    }

    public boolean isUserIdDuplicate(String userId) {
        return userMapper.isUserIdDuplicate(userId);
    }

    public boolean isRegisteredUser(UserLoginInfo userLoginInfo) {
        return userMapper.isRegisteredUser(userLoginInfo);
    }
}
