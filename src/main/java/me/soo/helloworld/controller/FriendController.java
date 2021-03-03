package me.soo.helloworld.controller;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.annotation.CurrentUser;
import me.soo.helloworld.service.FriendService;
import me.soo.helloworld.util.http.HttpResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friends")
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/request/{anotherUserId}")
    public ResponseEntity<Void> sendFriendRequest(@CurrentUser String userId, @PathVariable String anotherUserId) {
        friendService.sendFriendRequest(userId, anotherUserId);

        return HttpResponses.HTTP_RESPONSE_CREATED;
    }
}
