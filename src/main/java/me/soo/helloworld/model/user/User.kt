package me.soo.helloworld.model.user

import me.soo.helloworld.exception.InvalidUserInfoException
import me.soo.helloworld.util.encoder.PasswordEncoder
import java.util.Date
import javax.validation.constraints.Pattern

data class User @JvmOverloads constructor(

    @field:Pattern(
        regexp = "^[0-9a-zA-Z]{5,20}$",
        message = "아이디는 5 ~ 20자 이내, 영문 대/소문자 혹은 숫자만 허용합니다."
    )
    val userId: String,

    @field:Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{8,20}",
        message = "비밀번호를 입력해주세요 (8 ~ 20자 이내, 영문 대/소문자, 숫자, 특수문자를 각각 1개 이상 포함)"
    )
    val password: String,

    @field:Pattern(
        regexp = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$",
        message = "이메일 형식에 맞게 입력해주세요."
    )
    val email: String,

    @field:Pattern(
        regexp = "^[MFO]$",
        message = "성별을 입력해주세요. 'M'(Male), 'F'(Female), 'O'(Other) 중 하나만 입력이 가능합니다."
    )
    val gender: String,

    val birthday: Date,

    val originCountry: Int,

    val livingCountry: Int,

    val livingTown: Int,

    val aboutMe: String? = null,

    val profileImageName: String? = null,

    val profileImagePath: String? = null
) {

    companion object {

        fun verifyPassword(reqPassword: String, userPassword: String, passwordEncoder: PasswordEncoder) {
            if (!passwordEncoder.isMatch(reqPassword, userPassword))
                throw InvalidUserInfoException("입력하신 비밀번호가 일치하지 않습니다. 다시 한 번 확인해주세요.")
        }

        fun verifyEmail(isEmailValid: Boolean) {
            if (!isEmailValid)
                throw InvalidUserInfoException("해당 사용자가 존재하지 않거나 이메일이 일치하지 않습니다. 입력하신 정보를 다시 확인해 주세요.")
        }
    }

    fun createUserWithEncodedPassword(encodedPassword: String) =
        User(
            userId = userId,
            password = encodedPassword,
            email = email,
            gender = gender,
            birthday = birthday,
            originCountry = originCountry,
            livingCountry = livingCountry,
            livingTown = livingTown,
            aboutMe = aboutMe,
            profileImageName = profileImageName,
            profileImagePath = profileImagePath
        )
}