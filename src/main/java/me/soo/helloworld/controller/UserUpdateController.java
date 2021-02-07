package me.soo.helloworld.controller;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.annotation.CurrentUser;
import me.soo.helloworld.model.user.UserPasswordRequest;
import me.soo.helloworld.model.user.UserUpdateRequest;
import me.soo.helloworld.service.LoginService;
import me.soo.helloworld.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import static me.soo.helloworld.util.http.HttpResponses.HTTP_RESPONSE_OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserUpdateController {

    private final UserService userService;

    private final LoginService loginService;

    @PutMapping("/account/update/password")
    public ResponseEntity<Void> userPasswordUpdate(@CurrentUser String userId,
                                                   @RequestBody @Valid UserPasswordRequest userPasswordRequest) {

        userService.userPasswordUpdate(userId, userPasswordRequest);
        loginService.logout();

        return HTTP_RESPONSE_OK;
    }

    @PutMapping("/account/update/profile-image")
    public ResponseEntity<Void> userProfileImageUpdate(@CurrentUser String userId,
                                                       @RequestPart("profileImage") MultipartFile profileImage) {

        userService.userProfileImageUpdate(userId, profileImage);
        return HTTP_RESPONSE_OK;
    }

    @PutMapping("/account/update/info")
    public ResponseEntity<Void> userInfoUpdate(@CurrentUser String userId,
                                               @Valid @RequestBody UserUpdateRequest updateRequest) {
        userService.userInfoUpdate(userId, updateRequest);
        return HTTP_RESPONSE_OK;
    }
}

