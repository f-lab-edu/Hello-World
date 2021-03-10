package me.soo.helloworld.mapper;

import me.soo.helloworld.enumeration.FriendStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FriendMapper {

    public void sendFriendRequest(String userId, String targetId);

    public FriendStatus getFriendStatus(String userId, String targetId);

    public void deleteFriend(String userId, String targetId);

    public void updateFriendRequest(@Param("userId") String userId,
                                    @Param("targetId") String targetId,
                                    @Param("status") FriendStatus status);
}
