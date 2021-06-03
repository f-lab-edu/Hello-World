package me.soo.helloworld.model.user

data class LoginData @JvmOverloads constructor(

    val userId: String,

    val password: String,

    val token: String? = null
)