package me.soo.helloworld.mapper;

import me.soo.helloworld.enumeration.FriendStatus;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FriendMapper {

    public void sendFriendRequest(String userId, String anotherUserId);

    public FriendStatus getFriendStatus(String userId, String anotherUserId);


}
