package me.soo.helloworld.mapper;

import me.soo.helloworld.model.user.UserDataOnProfile;
import me.soo.helloworld.model.user.UserProfiles;
import me.soo.helloworld.util.Pagination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ProfileMapper {

    public Optional<UserDataOnProfile> getUserProfileData(String targetId, String userId);

    public List<UserProfiles> getUserProfiles(@Param("userId") String userId,
                                              @Param("pagination") Pagination pagination);
}
