package me.soo.helloworld.model.user

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class FindPasswordRequest(

    @field:NotBlank(message = "아이디를 입력하세요.")
    val userId: String,

    @field:Pattern(
        regexp = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$",
        message = "이메일 형식에 맞게 입력해주세요."
    )
    @field:NotBlank
    val email: String
)
