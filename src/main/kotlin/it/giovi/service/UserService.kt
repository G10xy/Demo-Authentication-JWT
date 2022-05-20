package it.giovi.service

import it.giovi.model.request.user.UserChangePasswordRequest
import it.giovi.model.request.user.UserFirstAccess
import it.giovi.model.request.user.UserInfoChangeRequest
import it.giovi.model.request.user.UserLostPasswordRequest
import it.giovi.model.request.auth.SignUpRequest
import it.giovi.model.response.UserResponse
import it.giovi.persistence.entity.UserEntity
import it.giovi.persistence.entity.UserRoleEntity.UserRoleEnum
import it.giovi.persistence.entity.UserStateEntity.UserStateEnum
import org.springframework.security.core.Authentication
import java.time.LocalDateTime


interface UserService {

    fun registerLastSignIn(authentication: Authentication)
    fun registerLastSignOut(authentication: Authentication)
    fun findUser(id: Long): UserResponse
    fun findAllRoles(): Iterable<String>
    fun findAllStates(): Iterable<String>
    fun daysBeforeExpiration(pwdExpirationDate: LocalDateTime): Long
    fun findAllDefaultSecurityQuestions(): Iterable<String>
    fun createUser(signUpRequest: SignUpRequest, authentication: Authentication)
    fun modifyUser(userId: Long, dtoRequest: UserInfoChangeRequest, authentication: Authentication)
    fun modifyPassword(userId: Long, userChangePasswordRequest: UserChangePasswordRequest)
    fun userActivation(userFirstAccess: UserFirstAccess)
    fun resetPassword(userId: Long, authentication: Authentication)
    fun lostPassword(userId: Long, request: UserLostPasswordRequest, authentication: Authentication)
    fun suspendUser(userId: Long, authentication: Authentication)
    fun enableUser(userId: Long, authentication: Authentication)
    fun deleteUser(userId: Long)
    fun createOtpToUser(user: UserEntity)
    fun findAllByFiltering(
        username: String?,
        name: String?,
        surname: String?,
        role: UserRoleEnum?,
        state: UserStateEnum?
    ): Iterable<UserResponse>
}