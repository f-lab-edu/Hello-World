package me.soo.helloworld.model.alarm;

import lombok.Builder;
import me.soo.helloworld.util.Pagination;

@Builder
public class AlarmListRequest {

    private final String userId;

    private final int offset;

    private final int limit;

    public static AlarmListRequest create(String userId, int pageNumber, Pagination pagination) {

        return AlarmListRequest.builder()
                                .userId(userId)
                                .offset(pagination.getMaxPageAlarm() * (Math.max(pageNumber, 1) - 1))
                                .limit(pagination.getMaxPageAlarm())
                                .build();
    }
}
