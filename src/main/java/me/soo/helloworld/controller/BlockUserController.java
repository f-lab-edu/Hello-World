package me.soo.helloworld.controller;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.annotation.CurrentUser;
import me.soo.helloworld.annotation.LoginRequired;
import me.soo.helloworld.service.BlockUserService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/block-users")
public class BlockUserController {

    private final BlockUserService blockUserService;

    @LoginRequired
    @PostMapping("/{targetId}")
    public void blockUser(@CurrentUser String userId, @PathVariable String targetId) {
        blockUserService.blockUser(userId, targetId);
    }
}
