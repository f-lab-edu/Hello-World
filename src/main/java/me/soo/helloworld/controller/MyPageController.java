package me.soo.helloworld.controller;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.annotation.CurrentUser;
import me.soo.helloworld.model.user.UserPasswordRequest;
import me.soo.helloworld.model.user.UserUpdateRequest;
import me.soo.helloworld.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import static me.soo.helloworld.util.http.HttpResponses.HTTP_RESPONSE_OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my-infos")
public class MyPageController {

    private final UserService userService;

    @PutMapping("/password")
    public ResponseEntity<Void> myPasswordUpdate(@CurrentUser String userId,
                                                   @RequestBody @Valid UserPasswordRequest userPasswordRequest) {

        userService.userPasswordUpdate(userId, userPasswordRequest);

        return HTTP_RESPONSE_OK;
    }

    @PutMapping("/profile-image")
    public ResponseEntity<Void> myProfileImageUpdate(@CurrentUser String userId,
                                                       @RequestPart("profileImage") MultipartFile profileImage) {

        userService.userProfileImageUpdate(userId, profileImage);

        return HTTP_RESPONSE_OK;
    }

    @PutMapping
    public ResponseEntity<Void> myInfoUpdate(@CurrentUser String userId,
                                               @Valid @RequestBody UserUpdateRequest updateRequest) {
        userService.userInfoUpdate(userId, updateRequest);

        return HTTP_RESPONSE_OK;
    }
}

