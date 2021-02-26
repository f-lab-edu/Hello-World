package me.soo.helloworld.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.soo.helloworld.util.handler.EnumCategoryTypeHandler;
import org.apache.ibatis.type.MappedTypes;

@Getter
@RequiredArgsConstructor
public enum LanguageLevel implements EnumCategory {

    BEGINNER(1),
    ELEMENTARY(2),
    INTERMEDIATE(3),
    UPPER_INTERMEDIATE(4),
    ADVANCED(5),
    PROFICIENCY(6),
    NATIVE(7);

    private final int category;

    @MappedTypes(LanguageLevel.class)
    public static class TypeHandler extends EnumCategoryTypeHandler<LanguageLevel> {

        public TypeHandler() {
            super(LanguageLevel.class);
        }
    }
}
