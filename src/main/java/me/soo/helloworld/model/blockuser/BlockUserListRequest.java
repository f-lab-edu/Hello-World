package me.soo.helloworld.model.blockuser;

import lombok.Builder;
import me.soo.helloworld.util.Pagination;

@Builder
public class BlockUserListRequest {

    private final String userId;

    private final Pagination pagination;

    static public BlockUserListRequest create(String userId, Pagination pagination) {

        return BlockUserListRequest.builder()
                                    .userId(userId)
                                    .pagination(pagination)
                                    .build();
    }
}
