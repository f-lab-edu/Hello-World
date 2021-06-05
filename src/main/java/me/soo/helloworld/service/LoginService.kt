package me.soo.helloworld.service

import me.soo.helloworld.model.user.LoginRequest

interface LoginService {

    fun login(loginRequest: LoginRequest)

    fun logout()

    fun getCurrentUserId(): String?
}
