package me.soo.helloworld.mapper;

import me.soo.helloworld.enumeration.FriendStatus;
import me.soo.helloworld.model.friend.FriendList;
import me.soo.helloworld.model.friend.FriendListRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FriendMapper {

    public void insertFriendRequest(String userId, String targetId);

    public FriendStatus getFriendStatus(String userId, String targetId);

    public void deleteFriend(String userId, String targetId);

    public void updateFriendRequest(@Param("userId") String userId,
                                    @Param("targetId") String targetId,
                                    @Param("status") FriendStatus status);

    public List<FriendList> getFriendList(FriendListRequest request);

    public Integer getFriendshipDuration(@Param("userId") String userId,
                                         @Param("targetId") String targetId,
                                         @Param("status") FriendStatus status);
}
