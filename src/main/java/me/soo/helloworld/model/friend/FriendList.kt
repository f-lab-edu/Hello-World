package me.soo.helloworld.model.friend

import me.soo.helloworld.enumeration.FriendStatus

data class FriendList(val id: Int, val friendId: String, val status: FriendStatus)
