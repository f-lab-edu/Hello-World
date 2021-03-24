package me.soo.helloworld.controller;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.annotation.CurrentUser;
import me.soo.helloworld.annotation.LoginRequired;
import me.soo.helloworld.model.blockuser.BlockUserList;
import me.soo.helloworld.service.BlockUserService;
import me.soo.helloworld.util.Pagination;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/block-users")
public class BlockUserController {

    private final BlockUserService blockUserService;

    @Value("${max.page.size:30}")
    private int pageSize;

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
    public List<BlockUserList> getBlockUserList(@CurrentUser String userId,
                                                @RequestParam(required = false) Integer cursor) {
        return blockUserService.getBlockUserList(userId, Pagination.create(cursor, pageSize));
    }
}
