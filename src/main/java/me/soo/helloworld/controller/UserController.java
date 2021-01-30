package me.soo.helloworld.controller;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.model.user.User;
import me.soo.helloworld.model.user.UserLoginInfo;
import me.soo.helloworld.service.LoginService;
import me.soo.helloworld.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static me.soo.helloworld.util.HttpResponses.*;

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
    public ResponseEntity<Void> userLogin(@Valid @RequestBody UserLoginInfo requestedUserLoginInfo, HttpSession httpSession) {

        try {
            UserLoginInfo storedUserLoginInfo = userService.getLoginUser(requestedUserLoginInfo);
            loginService.login(requestedUserLoginInfo, storedUserLoginInfo, httpSession);

            return HTTP_RESPONSE_OK;

        } catch (RuntimeException e) {
            return HTTP_RESPONSE_UNAUTHORIZED;
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> userLogout(HttpSession httpSession) {

        loginService.logout(httpSession);

        return HTTP_RESPONSE_NO_CONTENT;
    }
}
