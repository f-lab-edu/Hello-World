package me.soo.helloworld.service

import me.soo.helloworld.exception.InvalidUserInfoException
import me.soo.helloworld.mapper.UserMapper
import me.soo.helloworld.model.email.Email
import me.soo.helloworld.model.email.FIND_PASSWORD_BODY
import me.soo.helloworld.model.email.FIND_PASSWORD_TITLE
import me.soo.helloworld.model.user.FindPasswordRequest
import me.soo.helloworld.model.user.LoginData
import me.soo.helloworld.model.user.UpdateInfoRequest
import me.soo.helloworld.model.user.UpdatePasswordRequest
import me.soo.helloworld.model.user.User
import me.soo.helloworld.util.constant.CacheNames.MAIN_PAGE_KEY
import me.soo.helloworld.util.constant.CacheNames.MAIN_PAGE_VALUE
import me.soo.helloworld.util.constant.CacheNames.REDIS_CACHE_MANAGER
import me.soo.helloworld.util.constant.CacheNames.USER_PROFILE
import me.soo.helloworld.util.encoder.PasswordEncoder
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Service
class UserService(

    private val userMapper: UserMapper,

    private val fileService: FileService,

    private val passwordEncoder: PasswordEncoder,

    private val emailService: EmailService
) {

    @CacheEvict(key = MAIN_PAGE_KEY, cacheNames = [MAIN_PAGE_VALUE], cacheManager = REDIS_CACHE_MANAGER)
    fun signUp(user: User) =
        userMapper.insertUser(user.createUserWithEncodedPassword(passwordEncoder.encode(user.password)))

    @Transactional(readOnly = true)
    fun isUserIdExist(userId: String) = userMapper.isUserIdExist(userId)

    @Transactional(readOnly = true)
    fun getValidUserLoginData(userId: String, reqPassword: String): LoginData {
        val loginData = userMapper.getUserLoginDataById(userId)
            ?: throw InvalidUserInfoException("해당 사용자는 존재하지 않습니다. 아이디를 다시 확인해 주세요.")

        verifyPassword(reqPassword, loginData.password)
        return loginData
    }

    @CacheEvict(key = "#userId", cacheNames = [USER_PROFILE], cacheManager = REDIS_CACHE_MANAGER)
    fun updateProfile(userId: String, updateInfoRequest: UpdateInfoRequest) =
        userMapper.updateUserInfo(userId, updateInfoRequest)

    fun updatePassword(userId: String, updatePasswordRequest: UpdatePasswordRequest) {
        val encodedPassword = passwordEncoder.encode(updatePasswordRequest.newPassword)
        userMapper.updateUserPassword(userId, encodedPassword)
    }

    @CacheEvict(key = "#userId", cacheNames = [USER_PROFILE], cacheManager = REDIS_CACHE_MANAGER)
    fun updateProfileImage(userId: String, profileImage: MultipartFile) {
        userMapper.getUserProfileImageById(userId)?.let { fileService.deleteFile(it) }

        val newProfileImage = fileService.uploadFile(profileImage, userId)
        userMapper.updateUserProfileImage(userId, newProfileImage)
    }

    fun findPassword(findPasswordRequest: FindPasswordRequest) {
        if (!userMapper.isEmailValid(findPasswordRequest)) {
            throw InvalidUserInfoException("해당 사용자가 존재하지 않거나 이메일이 일치하지 않습니다. 입력하신 정보를 다시 확인해 주세요.")
        }

        val tempPassword = UUID.randomUUID().toString()
        val email = Email.create(findPasswordRequest.email, FIND_PASSWORD_TITLE, FIND_PASSWORD_BODY + tempPassword)
        emailService.sendEmail(email)
    }

    fun deleteMyAccount(userId: String, reqPassword: String) {
        val userPassword = userMapper.getUserPasswordById(userId)
        verifyPassword(reqPassword, userPassword)
        userMapper.deleteUser(userId)
    }

    @Transactional(readOnly = true)
    fun isUserActivated(userId: String) = userMapper.isUserActivated(userId)

    private fun verifyPassword(reqPassword: String, userPassword: String) =
        if (!passwordEncoder.isMatch(reqPassword, userPassword))
            throw InvalidUserInfoException("입력하신 비밀번호가 일치하지 않습니다. 다시 한 번 확인해주세요.") else Unit
}
