package me.soo.helloworld.mapper;

import me.soo.helloworld.model.file.FileData;
import me.soo.helloworld.model.user.User;
import me.soo.helloworld.model.user.UserUpdateRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.io.File;

@Mapper
public interface UserMapper {

    public void insertUser(User user);

    public boolean isUserIdDuplicate(String userId);

    public User getUserById(String userId);

    public FileData getUserProfileImageById(String userId);

    public void updateUserPassword(@Param("userId") String userId, @Param("password") String password);

    public void updateUser(
            @Param("userId") String userId,
            @Param("updateRequest") UserUpdateRequest updateRequest,
            @Param("imageData") FileData profileImageData
    );
}
