package me.soo.helloworld.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.soo.helloworld.util.handler.EnumCategoryTypeHandler;
import org.apache.ibatis.type.MappedTypes;

@Getter
@RequiredArgsConstructor
public enum LanguageStatus implements EnumCategory {

    LEARNING(1, 8),
    CAN_SPEAK(2, 4),
    NATIVE(3, 4);

    private final int category;

    private final int addLimit;

    @MappedTypes(LanguageStatus.class)
    public static class TypeHandler extends EnumCategoryTypeHandler<LanguageStatus> {

        public TypeHandler() {
            super(LanguageStatus.class);
        }
    }
}
