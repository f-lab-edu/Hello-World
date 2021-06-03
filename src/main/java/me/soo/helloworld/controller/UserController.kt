package me.soo.helloworld.controller

import me.soo.helloworld.model.user.FindPasswordRequest
import me.soo.helloworld.model.user.LoginRequest
import me.soo.helloworld.model.user.User
import me.soo.helloworld.service.LoginService
import me.soo.helloworld.service.UserService
import me.soo.helloworld.util.http.HttpResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/users")
class UserController(

    private val userService: UserService,

    private val loginService: LoginService
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    fun signUp(@Valid @RequestBody user: User) = userService.signUp(user)

    @GetMapping("/id-check")
    fun checkDuplicateId(@RequestParam userId: String) =
        if (!userService.isUserIdExist(userId)) HttpResponse.OK else HttpResponse.CONFLICT

    @PostMapping("/login")
    fun login(@Valid @RequestBody loginRequest: LoginRequest) = loginService.login(loginRequest)

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @GetMapping("/logout")
    fun logout() = loginService.logout()

    @PostMapping("/pw-finder")
    fun findPassword(@Valid @RequestBody findPasswordRequest: FindPasswordRequest) =
        userService.findPassword(findPasswordRequest)
}
