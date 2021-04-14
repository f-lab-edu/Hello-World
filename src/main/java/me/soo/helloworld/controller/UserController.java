package me.soo.helloworld.controller;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.annotation.CurrentUser;
import me.soo.helloworld.annotation.LoginRequired;
import me.soo.helloworld.model.user.*;
import me.soo.helloworld.service.LoginService;
import me.soo.helloworld.service.PushNotificationService;
import me.soo.helloworld.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static me.soo.helloworld.util.http.HttpResponses.HTTP_RESPONSE_CONFLICT;
import static me.soo.helloworld.util.http.HttpResponses.HTTP_RESPONSE_OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final LoginService loginService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public void userSignUp(@Valid @RequestBody User user) {
        userService.userSignUp(user);
    }

    @GetMapping("/id-check")
    public ResponseEntity<Void> isUserIdDuplicate(@RequestParam String userId) {
        if (userService.isUserIdExist(userId)) {
            return HTTP_RESPONSE_CONFLICT;
        }

        return HTTP_RESPONSE_OK;
    }

    @PostMapping("/login")
    public void userLogin(@Valid @RequestBody UserLoginRequest loginRequest) {
        loginService.login(loginRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @GetMapping("/logout")
    public void userLogout() {
        loginService.logout();
    }

    @PostMapping("/password-finder")
    public void userFindPassword(@Valid @RequestBody UserFindPasswordRequest findPasswordRequest) {
        userService.findUserPassword(findPasswordRequest);
    }
}
