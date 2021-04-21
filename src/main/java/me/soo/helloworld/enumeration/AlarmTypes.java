package me.soo.helloworld.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.soo.helloworld.util.handler.EnumCategoryTypeHandler;
import org.apache.ibatis.type.MappedTypes;

@Getter
@RequiredArgsConstructor
public enum AlarmTypes implements EnumCategory {

    FRIEND_REQUEST_RECEIVED(1, "친구 추가 요청을 받았습니다. 자세한 내용을 보시려면 여기를 클릭해주세요."),
    FRIEND_REQUEST_ACCEPTED(2, "친구 추가 요청이 수락되었습니다. 자세한 내용을 보시려면 여기를 클릭해주세요."),
    RECOMMENDATION_LEFT(3, "친구 중 한 명이 추천 글을 남겼습니다. 자세한 내용을 보시려면 여기를 클릭해주세요.");

    private final int category;

    private final String message;

    @MappedTypes(AlarmTypes.class)
    public static class TypeHandler extends EnumCategoryTypeHandler<AlarmTypes> {

        public TypeHandler() {
            super(AlarmTypes.class);
        }
    }
}
