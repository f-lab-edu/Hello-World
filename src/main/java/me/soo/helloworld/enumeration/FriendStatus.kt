package me.soo.helloworld.enumeration

import me.soo.helloworld.util.handler.EnumCategoryTypeHandler
import org.apache.ibatis.type.MappedTypes

enum class FriendStatus(private val category: Int) : EnumCategory {

    NONE(1),

    FRIEND_REQUESTED(2),

    FRIEND_REQUEST_RECEIVED(3),

    FRIEND(4);

    override fun getCategory() = category

    @MappedTypes(FriendStatus::class)
    class TypeHandler : EnumCategoryTypeHandler<FriendStatus>(FriendStatus::class.java)
}
