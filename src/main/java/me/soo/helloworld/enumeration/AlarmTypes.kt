package me.soo.helloworld.enumeration

import me.soo.helloworld.util.handler.EnumCategoryTypeHandler
import org.apache.ibatis.type.MappedTypes

enum class AlarmTypes(private val category: Int, val message: String) : EnumCategory {

    FRIEND_REQUEST_RECEIVED(1, "친구 추가 요청을 받았습니다. 자세한 내용을 확인하려면 여기를 클릭해주세요."),

    FRIEND_REQUEST_ACCEPTED(2, "상대방이 친구 추가 요청을 수락하였습니다. 자세한 내용을 확인하려면 여기를 클릭해주세요."),

    RECOMMENDATION_LEFT(3, "친구 한 명이 추천 글을 남겼습니다. 자세한 내용을 확인하려면 여기를 클릭해주세요.");

    override fun getCategory() = category

    @MappedTypes(AlarmTypes::class)
    class TypeHandler : EnumCategoryTypeHandler<AlarmTypes>(AlarmTypes::class.java)
}
