package me.soo.helloworld.controller;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.annotation.CurrentUser;
import me.soo.helloworld.service.FriendService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my-friends")
public class FriendController {

    private final FriendService friendService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/friend-requests/to/{targetId}")
    public void sendFriendRequest(@CurrentUser String userId, @PathVariable String targetId) {
        friendService.sendFriendRequest(userId, targetId);
    }

    @DeleteMapping("/friend-requests/to/{targetId}")
    public void cancelFriendRequest(@CurrentUser String userId, @PathVariable String targetId) {
        friendService.cancelFriendRequest(userId, targetId);
    }

    @PutMapping("/friend-requests/from/{targetId}/acceptance")
    public void acceptFriendRequest(@CurrentUser String userId, @PathVariable String targetId) {
        friendService.acceptFriendRequest(userId, targetId);
    }

    @DeleteMapping("/friend-requests/from/{targetId}/rejection")
    public void rejectFriendRequest(@CurrentUser String userId, @PathVariable String targetId) {
        friendService.rejectFriendRequest(userId, targetId);
    }

    @DeleteMapping("/{targetId}")
    public void unfriendFriend(@CurrentUser String userId, @PathVariable String targetId) {
        friendService.unfriendFriend(userId, targetId);
    }
}
