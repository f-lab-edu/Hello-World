package me.soo.helloworld.model.friend;

import lombok.AllArgsConstructor;
import lombok.Builder;
import me.soo.helloworld.enumeration.FriendStatus;

import static me.soo.helloworld.util.Pagination.MAX_PER_PAGE_FRIEND;

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
    static public FriendListRequest create(String userId, int pageNumber, FriendStatus status) {

        return FriendListRequest.builder()
                                .userId(userId)
                                .offset(MAX_PER_PAGE_FRIEND * (Math.max(pageNumber, 1) - 1))
                                .limit(MAX_PER_PAGE_FRIEND)
                                .status(status)
                                .build();
    }
}
