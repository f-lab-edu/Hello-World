package me.soo.helloworld.controller;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.model.User;
import me.soo.helloworld.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static me.soo.helloworld.util.HttpResponses.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

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
}
