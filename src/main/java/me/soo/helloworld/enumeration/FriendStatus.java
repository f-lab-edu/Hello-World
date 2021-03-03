package me.soo.helloworld.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.soo.helloworld.util.handler.EnumCategoryTypeHandler;
import org.apache.ibatis.type.MappedTypes;

@Getter
@RequiredArgsConstructor
public enum FriendStatus implements EnumCategory {

    NOT_YET(1),
    REQUESTED(2),
    RECEIVED(3),
    FRIENDED(4),
    BLOCKED(5);

    private final int category;

    @MappedTypes(FriendStatus.class)
    public static class TypeHandler extends EnumCategoryTypeHandler<FriendStatus> {

        public TypeHandler() {
            super(FriendStatus.class);
        }
    }
}
