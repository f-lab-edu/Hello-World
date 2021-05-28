package me.soo.helloworld.controller

import me.soo.helloworld.model.user.User
import me.soo.helloworld.model.user.UserFindPasswordRequest
import me.soo.helloworld.model.user.UserLoginRequest
import me.soo.helloworld.service.LoginService
import me.soo.helloworld.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/users")
class UserController(

    private val userService: UserService,

    private val loginService: LoginService) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    fun signUp(@Valid @RequestBody user: User) = userService.userSignUp(user)

    @GetMapping("/id-check")
    fun checkDuplicateId(@RequestParam userId: String): HttpStatus {
        return if (!userService.isUserIdExist(userId)) HttpStatus.OK else HttpStatus.CONFLICT
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody loginRequest: UserLoginRequest) = loginService.login(loginRequest)

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @GetMapping("/logout")
    fun logout() = loginService.logout()

    @PostMapping("/pw-finder")
    fun findPassword(@Valid @RequestBody findPasswordRequest: UserFindPasswordRequest) {
        userService.findPassword(findPasswordRequest)
    }
}