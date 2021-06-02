package me.soo.helloworld.model.user

import javax.validation.constraints.NotBlank

data class LoginRequest @JvmOverloads constructor(

    @field:NotBlank(message = "아이디를 입력하세요.")
    val userId: String,

    @field:NotBlank(message = "비밀번호를 입력하세요.")
    val password: String,

    val token: String? = null
)
