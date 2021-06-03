package me.soo.helloworld.mapper;

import me.soo.helloworld.model.file.FileData;
import me.soo.helloworld.model.user.User;
import me.soo.helloworld.model.user.FindPasswordRequest;
import me.soo.helloworld.model.user.LoginData;
import me.soo.helloworld.model.user.UpdateInfoRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    public void insertUser(User user);

    public boolean isUserIdExist(String userId);

    public LoginData getUserLoginDataById(String userId);

    public FileData getUserProfileImageById(String userId);

    public void updateUserPassword(@Param("userId") String userId, @Param("password") String password);

    public void updateUserProfileImage(@Param("userId") String userId, @Param("profileImage") FileData newProfileImage);

    public void updateUserInfo(@Param("userId") String userId, @Param("updateRequest") UpdateInfoRequest updateRequest);

    public String getUserPasswordById(String userId);

    public void deleteUser(String userId);

    public boolean isEmailValid(FindPasswordRequest findPasswordRequest);

    public boolean isUserActivated(String userId);
}
