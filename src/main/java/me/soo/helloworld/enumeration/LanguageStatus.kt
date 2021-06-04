package me.soo.helloworld.enumeration

import me.soo.helloworld.util.handler.EnumCategoryTypeHandler
import org.apache.ibatis.type.MappedTypes

enum class LanguageStatus(private val category: Int, val addLimit: Int) : EnumCategory {

    LEARNING(1, 8),

    CAN_SPEAK(2, 4),

    NATIVE(3, 4);

    override fun getCategory() = category

    @MappedTypes(LanguageStatus::class)
    class TypeHandler : EnumCategoryTypeHandler<LanguageStatus>(LanguageStatus::class.java)
}
