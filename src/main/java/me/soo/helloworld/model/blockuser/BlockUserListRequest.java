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
        int pageLimit = pagination.getMaxPageBlockUser();

        return BlockUserListRequest.builder()
                                    .userId(userId)
                                    .offset(pagination.calculateOffset(pageLimit, pageNumber))
                                    .limit(pageLimit)
                                    .build();
    }
}
