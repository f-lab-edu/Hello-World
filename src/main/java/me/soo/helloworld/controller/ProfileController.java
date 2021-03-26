package me.soo.helloworld.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.soo.helloworld.annotation.LoginRequired;
import me.soo.helloworld.model.user.UserProfile;
import me.soo.helloworld.service.ProfileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/profiles")
public class ProfileController {

    private final ProfileService profileService;

    @LoginRequired
    @GetMapping("/{userId}")
    public UserProfile getOneCountryName(@PathVariable String userId) {
        return profileService.getUserProfile(userId);
    }
}
