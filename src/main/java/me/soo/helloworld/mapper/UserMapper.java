package me.soo.helloworld.mapper;

import me.soo.helloworld.model.file.FileData;
import me.soo.helloworld.model.user.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserMapper {

    public void insertUser(User user);

    public boolean isUserIdExist(String userId);

    public Optional<UserLoginData> getUserLoginDataById(String userId);

    public Optional<TestUserLoginData> getTestUserLoginDataById(String userId);

    public FileData getUserProfileImageById(String userId);

    public void updateUserPassword(@Param("userId") String userId, @Param("password") String password);

    public void updateUserProfileImage(@Param("userId") String userId, @Param("profileImage") FileData newProfileImage);

    public void updateUserInfo(@Param("userId") String userId, @Param("updateRequest") UserUpdateRequest updateRequest);

    public String getUserPasswordById(String userId);

    public void deleteUser(String userId);

    public boolean isEmailValid(UserFindPasswordRequest findPasswordRequest);

    public boolean isUserActivated(String userId);
}
