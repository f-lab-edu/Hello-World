package me.soo.helloworld.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Pagination {

    private final int maxPageFriend;

    private final int maxPageBlockUser;

    private final int maxPageAlarm;

    public Pagination(@Value("${friend.max.page:30}") int maxPageFriend,
                      @Value("${block_user.max.page:30}") int maxPageBlockUser,
                      @Value("${alarm.max.page:30}") int maxPageAlarm) {
        this.maxPageFriend = maxPageFriend;
        this.maxPageBlockUser = maxPageBlockUser;
        this.maxPageAlarm = maxPageAlarm;
    }

    public int calculateOffset(int pageLimit, int pageNumber) {
        return pageLimit * (Math.max(pageNumber, 1) - 1);
    }
}
