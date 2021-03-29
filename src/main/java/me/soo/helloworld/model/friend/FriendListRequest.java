package me.soo.helloworld.model.friend;

import lombok.Builder;
import me.soo.helloworld.enumeration.FriendStatus;
import me.soo.helloworld.util.Pagination;

@Builder
public class FriendListRequest {

    private final String userId;

    private final Pagination pagination;

    private final FriendStatus status;

    static public FriendListRequest create(String userId, Pagination pagination, FriendStatus status) {

        return FriendListRequest.builder()
                .userId(userId)
                .pagination(pagination)
                .status(status)
                .build();
    }
}
