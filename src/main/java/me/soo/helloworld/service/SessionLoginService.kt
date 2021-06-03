package me.soo.helloworld.service

import me.soo.helloworld.exception.DuplicateLoginRequestException
import me.soo.helloworld.model.user.LoginRequest
import me.soo.helloworld.util.constant.SessionKeys.USER_ID
import org.springframework.stereotype.Service
import javax.servlet.http.HttpSession

@Service
class SessionLoginService(

    private val userService: UserService,

    private val pushNotificationService: PushNotificationService,

    private val httpSession: HttpSession

) : LoginService {

    override fun login(loginRequest: LoginRequest) {
        getCurrentUserId()?.let { throw DuplicateLoginRequestException("이미 로그인 되어 있는 사용자입니다.") }

        val loginData = userService.getValidUserLoginData(loginRequest.userId, loginRequest.password)
        httpSession.setAttribute(USER_ID, loginData.userId)
        updateTokenIfRequired(loginData.userId, loginData.token, loginRequest.token)
    }

    override fun logout() = httpSession.invalidate()

    override fun getCurrentUserId() = httpSession.getAttribute(USER_ID)?.toString()

    private fun updateTokenIfRequired(userId: String, oldToken: String?, newToken: String?) {
        if (!oldToken.equals(newToken)) {
            pushNotificationService.registerToken(userId, newToken)
        }
    }
}
