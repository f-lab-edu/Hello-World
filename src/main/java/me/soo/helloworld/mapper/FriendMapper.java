package me.soo.helloworld.mapper;

import me.soo.helloworld.enumeration.FriendStatus;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FriendMapper {

    public void sendFriendRequest(String userId, String targetId);

    public FriendStatus getFriendStatus(String userId, String targetId);

    public void deleteFriendRequest(String userId, String targetId);
}
