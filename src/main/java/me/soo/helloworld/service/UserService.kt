package me.soo.helloworld.service

import me.soo.helloworld.exception.InvalidUserInfoException
import me.soo.helloworld.mapper.UserMapper
import me.soo.helloworld.model.user.FindPasswordRequest
import me.soo.helloworld.model.user.LoginResponse
import me.soo.helloworld.model.user.UpdateInfoRequest
import me.soo.helloworld.model.user.UpdatePasswordRequest
import me.soo.helloworld.model.user.User
import me.soo.helloworld.util.encoder.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class UserService(

    private val userMapper: UserMapper,

    private val fileService: FileService,

    private val passwordEncoder: PasswordEncoder,

    private val emailService: EmailService
) {

    fun userSignUp(user: User) =
        userMapper.insertUser(user.createUserWithEncodedPassword(passwordEncoder.encode(user.password)))

    @Transactional(readOnly = true)
    fun isUserIdExist(userId: String) = userMapper.isUserIdExist(userId)

    @Transactional(readOnly = true)
    fun getUserLoginInfo(userId: String, reqPassword: String): LoginResponse {
        val loginRes = userMapper.getUserLoginDataById(userId)
            ?: throw InvalidUserInfoException("해당 사용자는 존재하지 않습니다. 아이디를 다시 확인해 주세요.")

        User.verifyPassword(reqPassword, loginRes.password, passwordEncoder)
        return loginRes
    }

    fun userInfoUpdate(userId: String, updateInfoRequest: UpdateInfoRequest) =
        userMapper.updateUserInfo(userId, updateInfoRequest)

    fun userPasswordUpdate(userId: String, updatePasswordRequest: UpdatePasswordRequest) {
        val encodedPassword = passwordEncoder.encode(updatePasswordRequest.newPassword)
        userMapper.updateUserPassword(userId, encodedPassword)
    }

    fun userProfileImageUpdate(userId: String, profileImage: MultipartFile) {
        userMapper.getUserProfileImageById(userId)?.let { fileService.deleteFile(it) }

        val newProfileImage = fileService.uploadFile(profileImage, userId)
        userMapper.updateUserProfileImage(userId, newProfileImage)
    }

    fun findPassword(findPasswordRequest: FindPasswordRequest) {
        User.verifyEmail(userMapper.isEmailValid(findPasswordRequest))

        val tempPassword = UUID.randomUUID().toString()
    }

    fun userDeleteAccount(userId: String, reqPassword: String) {
        val userPassword = userMapper.getUserPasswordById(userId)
        User.verifyPassword(reqPassword, userPassword, passwordEncoder)
        userMapper.deleteUser(userId)
    }

    @Transactional(readOnly = true)
    fun isUserActivated(userId: String) = userMapper.isUserActivated(userId)
}
