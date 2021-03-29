package me.soo.helloworld.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.soo.helloworld.annotation.LoginRequired;
import me.soo.helloworld.model.user.UserProfile;
import me.soo.helloworld.model.user.UserProfileList;
import me.soo.helloworld.service.ProfileService;
import me.soo.helloworld.util.Pagination;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/profiles")
public class ProfileController {

    private final ProfileService profileService;

    @Value("${max.page.size:30}")
    private int pageSize;

    @LoginRequired
    @GetMapping("/{userId}")
    public UserProfile getUserProfile(@PathVariable String userId) {
        return profileService.getUserProfile(userId);
    }

    @LoginRequired
    @GetMapping
    public List<UserProfileList> getUserProfilesList(@RequestParam(required = false) Integer cursor) {
        return profileService.getUserProfilesList(Pagination.create(cursor, pageSize));
    }
}
