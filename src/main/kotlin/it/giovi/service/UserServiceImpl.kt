package it.giovi.service

import it.giovi.errorhandling.exception.UserException
import it.giovi.errorhandling.exception.UserExceptionReason
import it.giovi.model.request.auth.SignUpRequest
import it.giovi.model.request.user.UserChangePasswordRequest
import it.giovi.model.request.user.UserFirstAccess
import it.giovi.model.request.user.UserInfoChangeRequest
import it.giovi.model.request.user.UserLostPasswordRequest
import it.giovi.model.response.UserResponse
import it.giovi.persistence.UserSpecifications
import it.giovi.persistence.entity.UserEntity
import it.giovi.persistence.entity.UserOtpEntity
import it.giovi.persistence.entity.UserRoleEntity
import it.giovi.persistence.entity.UserRoleEntity.UserRoleEnum
import it.giovi.persistence.entity.UserStateEntity
import it.giovi.persistence.entity.UserStateEntity.UserStateEnum
import it.giovi.persistence.repository.UserOtpRepository
import it.giovi.persistence.repository.UserRepository
import it.giovi.security.JwtUserDetailsImpl
import it.giovi.security.SecurityProperties
import it.giovi.security.UserDetailsServiceImpl
import it.giovi.service.mapper.UserMapper
import it.giovi.util.Utility
import net.bytebuddy.utility.RandomString
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.streams.toList


@Service
class UserServiceImpl(
    private val bcryptEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
    private val userRoleService: UserRoleService,
    private val userStateService: UserStateService,
    private val userDetailsService: UserDetailsServiceImpl,
    private val otpRepository: UserOtpRepository,
    private val securityProperties: SecurityProperties,
    private val userSecurityService: UserSecurityService
) : UserService {

    companion object {
        private val log = LoggerFactory.getLogger(UserServiceImpl::class.java)
    }

    val userMapper: UserMapper = Mappers.getMapper(UserMapper::class.java)


    @Transactional
    override fun registerLastSignIn(authentication: Authentication) {
        val user: JwtUserDetailsImpl = authentication.principal as JwtUserDetailsImpl
        log.info("User login: " + user.username)
        userSecurityService.registerSuccessLogin(user.username)
        userRepository.setUserLastSignIn(user.username, userStateService.getUserState(UserStateEnum.ACTIVE))
    }

    @Transactional
    override fun registerLastSignOut(authentication: Authentication) {
        val user = authentication.principal as JwtUserDetailsImpl
        log.info("User logout: " + user.username)
        userRepository.setUserLastSignOut(user.username, userStateService.getUserState(UserStateEnum.ACTIVE));
    }

    override fun daysBeforeExpiration(pwdExpirationDate: LocalDateTime): Long {
        return Utility.differenceBetween(LocalDateTime.now(), pwdExpirationDate, ChronoUnit.DAYS)
    }

    override fun findUser(id: Long): UserResponse {
        return userMapper.userEntityToUserResponse(userDetailsService.findById(id))
    }

    override fun findAllRoles(): Iterable<String> {
        return userRoleService.getAllRoles().stream().map { role: UserRoleEntity -> role.role.name }.toList()
    }

    override fun findAllStates(): Iterable<String> {
        return userStateService.getAllStates().stream().map { state: UserStateEntity -> state.state.name }.toList()
    }

    override fun findAllDefaultSecurityQuestions(): Iterable<String> {
        return Utility.readResourceFile(securityProperties.defaultQuestionFile).split("\\r?\\n")
    }

    override fun findAllByFiltering(
        username: String?,
        name: String?,
        surname: String?,
        role: UserRoleEnum?,
        state: UserStateEnum?
    ): Iterable<UserResponse> {
        return userRepository.findAll(
            UserSpecifications.userByUsernameLike(username!!)
                .and(UserSpecifications.userByUsernameLike(name!!))
                .and(UserSpecifications.userBySurnameLike(surname!!))
                .and(UserSpecifications.userByRole(role!!))
                .and(UserSpecifications.userByState(state!!))
        ).map { user -> userMapper.userEntityToUserResponse(user) }.toList()
    }

    @Transactional
    override fun suspendUser(userId: Long, authentication: Authentication) {
        val user = userDetailsService.findById(userId)
        if (user.userState.state == UserStateEnum.SUSPENDED) throw UserException(UserExceptionReason.USER_ALREADY_SUSPENDED)
        user.userState = userStateService.getUserState(UserStateEnum.SUSPENDED)
        user.suspensionDate = LocalDateTime.now()
    }

    @Transactional
    override fun enableUser(userId: Long, authentication: Authentication) {
        val user = userDetailsService.findById(userId)
        if (user.userState.state == UserStateEnum.ACTIVE) throw UserException(UserExceptionReason.USER_ALREADY_ENABLED)
        user.userState = userStateService.getUserState(UserStateEnum.ACTIVE)
        user.enableDate = LocalDateTime.now()
    }

    @Transactional
    override fun deleteUser(userId: Long, authentication: Authentication) {
        val user = userDetailsService.findById(userId)
        if (user.userState.state == UserStateEnum.DELETED) throw UserException(UserExceptionReason.USER_ALREADY_DELETED)
        user.userState = userStateService.getUserState(UserStateEnum.DELETED)
        user.deletedDate = LocalDateTime.now()
    }

    @Transactional
    override fun modifyUser(userId: Long, dtoRequest: UserInfoChangeRequest, authentication: Authentication) {
        val user = userDetailsService.findById(userId)
        user.name = dtoRequest.name
        user.surname = dtoRequest.surname
        user.userRole = userRoleService.getUserRole(dtoRequest.role)
    }

    @Transactional
    override fun modifyPassword(userId: Long, userChangePasswordRequest: UserChangePasswordRequest) {
        val user = userDetailsService.findById(userId)
        if (!bcryptEncoder.matches(userChangePasswordRequest.password, user.password)) {
            throw UserException(UserExceptionReason.PASSWORD_NOT_VALID)
        }
        user.lastPasswordChange = LocalDateTime.now()
        user.password = bcryptEncoder.encode(userChangePasswordRequest.newPassword)
    }

    override fun lostPassword(userId: Long, request: UserLostPasswordRequest, authentication: Authentication) {
        val user = userDetailsService.findById(userId)
        if (user.secQuestion != request.question) throw UserException(UserExceptionReason.SEC_QUESTION_WRONG)
        if (user.secAnswer != request.answer) throw UserException(UserExceptionReason.SEC_ANSWER_WRONG)
        user.id?.let { this.resetPassword(it, authentication) }
    }

    @Transactional
    override fun resetPassword(userId: Long, authentication: Authentication) {
        val user = userDetailsService.findById(userId)
        user.password = null
        user.lastPasswordResetDate = LocalDateTime.now()
        user.userState = userStateService.getUserState(UserStateEnum.ACTIVE)
    }

    @Transactional
    override fun createUser(signUpRequest: SignUpRequest, authentication: Authentication) {
        if (userRepository.findOne(UserSpecifications.userByUsername(signUpRequest.username).and(UserSpecifications.userNotDeleted())).isPresent) {
            throw UserException(UserExceptionReason.USERNAME_ALREADY_ASSIGNED)
        }
        val user = UserEntity(
            username = signUpRequest.username.trim(),
            name = signUpRequest.name.trim(),
            surname = signUpRequest.surname.trim(),
            userRole = userRoleService.getUserRole(signUpRequest.role),
            userState = userStateService.getUserState(UserStateEnum.INACTIVE))
        userRepository.saveAndFlush(user)
    }

    @Transactional
    override fun userActivation(userFirstAccess: UserFirstAccess) {
        val user = userRepository.findOne(UserSpecifications.userByUsername(userFirstAccess.username).and(UserSpecifications.userByState(UserStateEnum.INACTIVE)))
            .orElseThrow { throw UserException(UserExceptionReason.USERNAME_NOT_VALID) }
        val otp: UserOtpEntity = otpRepository.findValidTokenByUser(user, LocalDateTime.now())
            .orElseThrow { throw UserException(UserExceptionReason.OTP_EXPIRED) }
        if (!bcryptEncoder.matches(userFirstAccess.otp, otp.token)) {
            throw UserException(UserExceptionReason.OTP_NOT_VALID)
        }
        val encodedPassword = bcryptEncoder.encode(user.password?.trim())
        user.userState = userStateService.getUserState(UserStateEnum.ACTIVE)
        user.password = encodedPassword
        if (user.userRole.role == UserRoleEnum.ROLE_ADMIN) {
            user.pwdExpirationDate = LocalDateTime.now().plusYears(100)
        } else {
            user.pwdExpirationDate = LocalDateTime.now().plusDays(50)
        }
        user.secQuestion = userFirstAccess.question.trim()
        user.secAnswer = userFirstAccess.answer.trim()
        otpRepository.delete(otp)
    }

    @Transactional
    override fun createOtpToUser(user: UserEntity) {
        val otp: String = RandomString.make(14)
        val encodedOtp = bcryptEncoder.encode(otp)
        val userOtp = UserOtpEntity(encodedOtp, user)
        otpRepository.save(userOtp)
    }


}