package me.soo.helloworld.mapper;

import me.soo.helloworld.model.user.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    public void insertUser(User user);

    public boolean isUserIdDuplicate(String userId);

    public User getUserById(String userId);
}
