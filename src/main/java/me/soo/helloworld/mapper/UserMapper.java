package me.soo.helloworld.mapper;

import me.soo.helloworld.model.user.User;
import me.soo.helloworld.model.user.UserLoginInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    public void insertUser(User user);
    public boolean isUserIdDuplicate(String userId);
    public UserLoginInfo getRegisteredUserInfo(UserLoginInfo userLoginInfo);
}
