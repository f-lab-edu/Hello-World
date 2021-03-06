package me.soo.helloworld.controller;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.annotation.CurrentUser;
import me.soo.helloworld.annotation.LoginRequired;
import me.soo.helloworld.model.user.UserPasswordRequest;
import me.soo.helloworld.model.user.UserUpdateRequest;
import me.soo.helloworld.service.LoginService;
import me.soo.helloworld.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my-infos")
public class MyPageController {

    private final UserService userService;

    private final LoginService loginService;

    @LoginRequired
    @PutMapping("/password")
    public void myPasswordUpdate(@CurrentUser String userId,
                                                   @RequestBody @Valid UserPasswordRequest userPasswordRequest) {
        userService.userPasswordUpdate(userId, userPasswordRequest);
    }

    @LoginRequired
    @PutMapping("/profile-image")
    public void myProfileImageUpdate(@CurrentUser String userId,
                                                       @RequestPart("profileImage") MultipartFile profileImage) {
        userService.userProfileImageUpdate(userId, profileImage);
    }

    @LoginRequired
    @PutMapping
    public void myInfoUpdate(@CurrentUser String userId,
                                               @Valid @RequestBody UserUpdateRequest updateRequest) {
        userService.userInfoUpdate(userId, updateRequest);
    }

    @LoginRequired
    @DeleteMapping("/account")
    public void myAccountDelete(@CurrentUser String userId, @RequestParam String requestPassword) {
        userService.userDeleteAccount(userId, requestPassword);
        loginService.logout();
    }
}

