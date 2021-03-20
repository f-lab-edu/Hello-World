package me.soo.helloworld.controller;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.annotation.CurrentUser;
import me.soo.helloworld.annotation.LoginRequired;
import me.soo.helloworld.service.BlockUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @LoginRequired
    @DeleteMapping ("/{targetId}")
    public void unBlockUser(@CurrentUser String userId, @PathVariable String targetId) {
        blockUserService.unblockUser(userId, targetId);
    }

    @LoginRequired
    @GetMapping
    public List<String> getBlockUserList(@CurrentUser String userId,
                                         @RequestParam(defaultValue = "1") Integer pageNumber) {
        return blockUserService.getBlockUserList(userId, pageNumber);
    }
}
