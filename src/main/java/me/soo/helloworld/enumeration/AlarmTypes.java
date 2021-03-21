package me.soo.helloworld.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.soo.helloworld.util.handler.EnumCategoryTypeHandler;
import org.apache.ibatis.type.MappedTypes;

@Getter
@RequiredArgsConstructor
public enum AlarmTypes implements EnumCategory {

    FRIEND_REQUEST_RECEIVED(1),
    FRIEND_REQUEST_ACCEPTED(2),
    RECOMMENDATION_LEFT(3);

    private final int category;

    @MappedTypes(AlarmTypes.class)
    public static class TypeHandler extends EnumCategoryTypeHandler<AlarmTypes> {

        public TypeHandler() {
            super(AlarmTypes.class);
        }
    }
}
