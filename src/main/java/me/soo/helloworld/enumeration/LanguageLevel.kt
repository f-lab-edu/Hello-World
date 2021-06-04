package me.soo.helloworld.enumeration

import me.soo.helloworld.util.handler.EnumCategoryTypeHandler
import org.apache.ibatis.type.MappedTypes

enum class LanguageLevel(private val category: Int) : EnumCategory {

    BEGINNER(1),

    ELEMENTARY(2),

    INTERMEDIATE(3),

    UPPER_INTERMEDIATE(4),

    ADVANCED(5),

    PROFICIENCY(6),

    NATIVE(7);

    override fun getCategory() = category

    @MappedTypes(LanguageLevel::class)
    class TypeHandler : EnumCategoryTypeHandler<LanguageLevel>(LanguageLevel::class.java)
}
