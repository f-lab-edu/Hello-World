package me.soo.helloworld.controller

import me.soo.helloworld.annotation.CurrentUser
import me.soo.helloworld.annotation.LoginRequired
import me.soo.helloworld.service.FriendService
import me.soo.helloworld.util.Pagination
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/my-friends")
class FriendController(private val friendService: FriendService) {

    @Value("\${max.page.size: 30}")
    private val pageSize: Int = 0

    @LoginRequired
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/friend-requests/to/{targetId}")
    fun sendFriendRequest(@CurrentUser userId: String, @PathVariable targetId: String) =
        friendService.sendFriendRequest(userId, targetId)

    @ResponseStatus(HttpStatus.CREATED)
    @DeleteMapping("/friend-requests/to/{targetId}")
    fun cancelFriendRequest(@CurrentUser userId: String, @PathVariable targetId: String) =
        friendService.cancelFriendRequest(userId, targetId)

    @LoginRequired
    @PutMapping("/friend-requests/from/{targetId}/acceptance")
    fun acceptFriendRequest(@CurrentUser userId: String, @PathVariable targetId: String) =
        friendService.acceptFriendRequest(userId, targetId)

    @LoginRequired
    @DeleteMapping("/friend-requests/from/{targetId}/rejection")
    fun rejectFriendRequest(@CurrentUser userId: String, @PathVariable targetId: String) =
        friendService.rejectFriendRequest(userId, targetId)

    @LoginRequired
    @DeleteMapping("/{targetId}")
    fun unfriendFriend(@CurrentUser userId: String, @PathVariable targetId: String) =
        friendService.unfriendFriend(userId, targetId)

    @LoginRequired
    @GetMapping
    fun getFriendList(@CurrentUser userId: String, @RequestParam(required = false) cursor: Int?) =
        friendService.getFriendList(userId, Pagination.create(cursor, pageSize))

    @LoginRequired
    @GetMapping("/friend-requests")
    fun getFriendRequestList(@CurrentUser userId: String, @RequestParam(required = false) cursor: Int?) =
        friendService.getFriendRequestList(userId, Pagination.create(cursor, pageSize))
}
