package me.soo.helloworld.mapper;

import me.soo.helloworld.model.user.User;
import me.soo.helloworld.model.user.UserUpdate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    public void insertUser(User user);

    public boolean isUserIdDuplicate(String userId);

    public User getUserById(String userId);

    public void updateUserPassword(@Param("userId") String userId, @Param("password") String password);

    public void updateUser(UserUpdate userUpdate);
}
