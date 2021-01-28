package me.soo.helloworld.mapper;

import me.soo.helloworld.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    public void insertUser(User user);

    public boolean isUserIdDuplicate(String userId);
}
