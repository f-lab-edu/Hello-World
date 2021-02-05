package me.soo.helloworld.repository;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.mapper.UserMapper;
import me.soo.helloworld.model.file.FileData;
import me.soo.helloworld.model.user.User;
import me.soo.helloworld.model.user.UserUpdateRequest;
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

    public User getUserById(String userId) {
        return userMapper.getUserById(userId);
    }

    public FileData getUserProfileImageById(String userId) {
        return userMapper.getUserProfileImageById(userId);
    }

    public void updateUserPassword(String userId, String password) {
        userMapper.updateUserPassword(userId, password);
    }

    public void updateUser(String userId, UserUpdateRequest userUpdate, FileData profileImageData) {
        userMapper.updateUser(userId, userUpdate, profileImageData);
    }

}
