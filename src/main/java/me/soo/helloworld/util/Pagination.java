package me.soo.helloworld.util;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Pagination {

    private final Integer cursor;

    private final int pageSize;

    public static Pagination create(Integer cursor, int size) {

        return Pagination.builder()
                        .cursor(cursor)
                        .pageSize(size)
                        .build();
    }

    public int calculateOffset(int pageLimit, int pageNumber) {
        return pageLimit * (Math.max(pageNumber, 1) - 1);
    }
}
