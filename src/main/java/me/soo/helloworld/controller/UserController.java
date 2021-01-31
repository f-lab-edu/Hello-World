package me.soo.helloworld.controller;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.model.user.User;
import me.soo.helloworld.model.user.UserIdAndPassword;
import me.soo.helloworld.service.LoginService;
import me.soo.helloworld.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Void> userLogin(@Valid @RequestBody UserIdAndPassword requestedIdAndPassword) {

        try {
            UserIdAndPassword storedUserIdAndPassword = userService.getLoginUser(requestedIdAndPassword);

           if (storedUserIdAndPassword == null) {
               return HTTP_RESPONSE_NOT_FOUND;
           }

            loginService.login(requestedIdAndPassword, storedUserIdAndPassword);

            return HTTP_RESPONSE_OK;

        } catch (RuntimeException e) {
            return HTTP_RESPONSE_UNAUTHORIZED;
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> userLogout() {

        loginService.logout();

        return HTTP_RESPONSE_NO_CONTENT;
    }
}
