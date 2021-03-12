package me.soo.helloworld.controller;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.annotation.CurrentUser;
import me.soo.helloworld.annotation.LoginRequired;
import me.soo.helloworld.model.friend.FriendList;
import me.soo.helloworld.service.FriendService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my-friends")
public class FriendController {

    private final FriendService friendService;

    @LoginRequired
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/friend-requests/to/{targetId}")
    public void sendFriendRequest(@CurrentUser String userId, @PathVariable String targetId) {
        friendService.sendFriendRequest(userId, targetId);
    }

    @LoginRequired
    @DeleteMapping("/friend-requests/to/{targetId}")
    public void cancelFriendRequest(@CurrentUser String userId, @PathVariable String targetId) {
        friendService.cancelFriendRequest(userId, targetId);
    }

    @LoginRequired
    @PutMapping("/friend-requests/from/{targetId}/acceptance")
    public void acceptFriendRequest(@CurrentUser String userId, @PathVariable String targetId) {
        friendService.acceptFriendRequest(userId, targetId);
    }

    @LoginRequired
    @DeleteMapping("/friend-requests/from/{targetId}/rejection")
    public void rejectFriendRequest(@CurrentUser String userId, @PathVariable String targetId) {
        friendService.rejectFriendRequest(userId, targetId);
    }

    @LoginRequired
    @DeleteMapping("/{targetId}")
    public void unfriendFriend(@CurrentUser String userId, @PathVariable String targetId) {
        friendService.unfriendFriend(userId, targetId);
    }

    @LoginRequired
    @GetMapping
    public List<FriendList> getFriendList(@CurrentUser String userId,
                                                          @RequestParam(defaultValue = "1") Integer pageNumber) {
        return friendService.getFriendList(userId, pageNumber);
    }

    @LoginRequired
    @GetMapping("/friend-requests")
    public List<FriendList> getFriendRequestList(@CurrentUser String userId,
                                                          @RequestParam(defaultValue = "1") Integer pageNumber) {
        return friendService.getFriendRequestList(userId, pageNumber);
    }

    @LoginRequired
    @PostMapping("/block/{targetId}")
    public void blockUser(@CurrentUser String userId, @PathVariable String targetId) {
        friendService.blockUser(userId, targetId);
    }
}
