package me.soo.helloworld.controller

import me.soo.helloworld.annotation.CurrentUser
import me.soo.helloworld.annotation.LoginRequired
import me.soo.helloworld.model.user.UpdateInfoRequest
import me.soo.helloworld.model.user.UpdatePasswordRequest
import me.soo.helloworld.service.LoginService
import me.soo.helloworld.service.UserService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid

@RestController
@RequestMapping("/my-infos")
class MyPageController(

    private val userService: UserService,

    private val loginService: LoginService
) {

    @LoginRequired
    @PutMapping("/password")
    fun updatePassword(@CurrentUser userId: String, @RequestBody @Valid updatePasswordRequest: UpdatePasswordRequest) =
        userService.userPasswordUpdate(userId, updatePasswordRequest)

    @LoginRequired
    @PutMapping("/profile-image")
    fun updateProfileImage(@CurrentUser userId: String, @RequestPart profileImage: MultipartFile) =
        userService.userProfileImageUpdate(userId, profileImage)

    @LoginRequired
    @PutMapping
    fun updateInfo(@CurrentUser userId: String, @Valid @RequestBody updateInfoRequest: UpdateInfoRequest) =
        userService.userInfoUpdate(userId, updateInfoRequest)

    @LoginRequired
    @DeleteMapping("/account")
    fun deleteAccount(@CurrentUser userId: String, @RequestParam password: String) {
        userService.userDeleteAccount(userId, password)
        loginService.logout()
    }
}
