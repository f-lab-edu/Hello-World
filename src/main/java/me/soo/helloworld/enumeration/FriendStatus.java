package me.soo.helloworld.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.soo.helloworld.util.handler.EnumCategoryTypeHandler;
import org.apache.ibatis.type.MappedTypes;

@Getter
@RequiredArgsConstructor
public enum FriendStatus implements EnumCategory {

    NONE(1),
    FRIEND_REQUESTED(2),
    FRIEND_REQUEST_RECEIVED(3),
    FRIEND(4);

    private final int category;

    @MappedTypes(FriendStatus.class)
    public static class TypeHandler extends EnumCategoryTypeHandler<FriendStatus> {

        public TypeHandler() {
            super(FriendStatus.class);
        }
    }
}
