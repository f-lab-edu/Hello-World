package me.soo.helloworld.model.user

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class UpdatePasswordRequest(

    @field:NotBlank(message = "현재 비밀번호를 입력해주세요.")
    val currentPassword: String,

    @field:Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{8,20}",
        message = "새로운 비밀번호를 입력해주세요 (8 ~ 20자 이내, 영문 대/소문자, 숫자, 특수문자를 각각 1개 이상 포함)"
    )
    val newPassword: String,

    @field:NotBlank(message = "새로운 비밀번호를 다시 한 번 입력해주세요.")
    val checkNewPassword: String
)
