package me.soo.helloworld.mapper;

import me.soo.helloworld.model.user.UserDataOnProfile;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface ProfileMapper {
    public Optional<UserDataOnProfile> getUserProfileData(String userId);
}
