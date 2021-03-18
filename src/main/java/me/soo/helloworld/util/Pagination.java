package me.soo.helloworld.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Pagination {

    private final int maxPageFriend;

    public Pagination(@Value("${max.page.friend:30}") int maxPageFriend) {
        this.maxPageFriend = maxPageFriend;
    }

    public int calculateOffset(int pageLimit, int pageNumber) {
        return pageLimit * (Math.max(pageNumber, 1) - 1);
    }
}
