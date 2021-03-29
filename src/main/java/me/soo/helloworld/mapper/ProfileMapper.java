package me.soo.helloworld.mapper;

import me.soo.helloworld.model.user.UserDataOnProfile;
import me.soo.helloworld.model.user.UserProfileList;
import me.soo.helloworld.util.Pagination;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ProfileMapper {

    public Optional<UserDataOnProfile> getUserProfileData(String userId);

    public List<UserProfileList> getUserProfilesList(Pagination pagination);
}
