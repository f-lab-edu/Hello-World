package me.soo.helloworld.model.friend;

import lombok.AllArgsConstructor;
import lombok.Builder;
import me.soo.helloworld.enumeration.FriendStatus;
import me.soo.helloworld.util.Pagination;

@Builder
@AllArgsConstructor
public class FriendListRequest {

    private final String userId;

    private final int offset;

    private final int limit;

    private final FriendStatus status;

    /*
        Math.max(pageNumber, 1): pageNumber 가 음수로 들어와도 기본값을 1로 설정해서 맨 첫 페이지가 보이도록 설정
     */
    static public FriendListRequest create(String userId, int pageNumber, Pagination pagination, FriendStatus status) {

        return FriendListRequest.builder()
                                .userId(userId)
                                .offset(pagination.getMaxPageFriend() * (Math.max(pageNumber, 1) - 1))
                                .limit(pagination.getMaxPageFriend())
                                .status(status)
                                .build();
    }
}
