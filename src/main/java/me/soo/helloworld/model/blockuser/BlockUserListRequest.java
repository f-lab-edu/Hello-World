package me.soo.helloworld.model.blockuser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import me.soo.helloworld.util.Pagination;

@Builder
@AllArgsConstructor
public class BlockUserListRequest {

    private final String userId;

    private final int offset;

    private final int limit;

    static public BlockUserListRequest create(String userId, int pageNumber, Pagination pagination) {

        return BlockUserListRequest.builder()
                                    .userId(userId)
                                    .offset(pagination.getMaxPageBlockUser() * (Math.max(pageNumber, 1) - 1))
                                    .limit(pagination.getMaxPageBlockUser())
                                    .build();
    }
}
