package me.soo.helloworld.controller;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.annotation.CurrentUser;
import me.soo.helloworld.model.user.*;
import me.soo.helloworld.service.LoginService;
import me.soo.helloworld.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


import static me.soo.helloworld.util.http.HttpResponses.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final LoginService loginService;

    @PostMapping("/signup")
    public ResponseEntity<Void> userSignUp(@Valid @RequestBody User user) {

        userService.userSignUp(user);

        return HTTP_RESPONSE_CREATED;
    }

    @GetMapping("/idcheck")
    public ResponseEntity<Void> isUserIdDuplicate(@RequestParam String userId) {

        if (userService.isUserIdDuplicate(userId)) {
            return HTTP_RESPONSE_CONFLICT;
        }

        return HTTP_RESPONSE_OK;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> userLogin(@Valid @RequestBody UserLoginRequest loginRequest) {

        loginService.login(loginRequest);

        return HTTP_RESPONSE_OK;
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> userLogout() {

        loginService.logout();

        return HTTP_RESPONSE_NO_CONTENT;
    }

    @PostMapping("/password-finder")
    public void userFindPassword(@Valid @RequestBody UserFindPasswordRequest findPasswordRequest) {

        userService.findUserPassword(findPasswordRequest);
    }

    @DeleteMapping("/account")
    public ResponseEntity<Void> userDeleteAccount(@CurrentUser String userId, @RequestParam String requestPassword) {

        userService.userDeleteAccount(userId, requestPassword);

        return HTTP_RESPONSE_OK;
    }
}
