package me.soo.helloworld.model.user

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class UserFindPasswordRequest(

    @field:NotBlank(message = "아이디를 입력하세요.")
    val userId: String,

    @field:Email(message = "이메일을 형식에 맞게 입력해주세요")
    @field:NotBlank
    val email: String
)