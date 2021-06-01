package me.soo.helloworld.model.user

import javax.validation.constraints.Pattern

data class UpdateInfoRequest(

    @field:Pattern(
        regexp = "^[MFO]$",
        message = "성별은 'M'(Male), 'F'(Female), 'O'(Others) 중 하나만 입력이 가능합니다."
    )
    val gender: String,

    val livingCountry: Int,

    val livingTown: Int,

    val aboutMe: String?
)
