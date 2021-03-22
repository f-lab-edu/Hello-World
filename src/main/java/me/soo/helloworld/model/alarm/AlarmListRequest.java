package me.soo.helloworld.model.alarm;

import lombok.Builder;
import me.soo.helloworld.util.Pagination;

@Builder
public class AlarmListRequest {

    private final String userId;

    private final Pagination pagination;

    public static AlarmListRequest create(String userId, Pagination pagination) {

        return AlarmListRequest.builder()
                                .userId(userId)
                                .pagination(pagination)
                                .build();
    }
}
