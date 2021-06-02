package me.soo.helloworld.model.user

data class LoginResponse @JvmOverloads constructor(

    val userId: String,

    val password: String,

    val token: String? = null
)