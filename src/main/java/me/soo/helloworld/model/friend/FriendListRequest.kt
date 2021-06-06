package me.soo.helloworld.model.friend

import me.soo.helloworld.enumeration.FriendStatus
import me.soo.helloworld.util.Pagination

data class FriendListRequest(val userId: String, val pagination: Pagination, val status: FriendStatus) {

    companion object {
        @JvmStatic
        fun create(userId: String, pagination: Pagination, status: FriendStatus) =
            FriendListRequest(userId, pagination, status)
    }
}
