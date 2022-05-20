package it.giovi.service

import it.giovi.errorhandling.exception.ApplicationException
import it.giovi.errorhandling.exception.UserException
import it.giovi.errorhandling.exception.UserExceptionReason
import it.giovi.persistence.UserSpecifications
import it.giovi.persistence.entity.UserRoleEntity
import it.giovi.persistence.entity.UserStateEntity
import it.giovi.persistence.repository.UserRepository
import it.giovi.security.JwtAuthenticationEntryPoint
import it.giovi.security.JwtUserDetailsImpl
import it.giovi.security.SecurityProperties
import it.giovi.security.UserDetailsServiceImpl
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
open class UserSecurityServiceImpl(
    private val userDetailsServiceImpl: UserDetailsServiceImpl,
    private val userStateService: UserStateService
) : UserSecurityService {

    private val log = LoggerFactory.getLogger(UserSecurityServiceImpl::class.java)


    private var cacheFailedLogins: MutableMap<String, Int> = Collections.synchronizedMap(mutableMapOf())

    @Transactional(noRollbackFor = [ApplicationException::class])
    override fun registerFailedLogin(username: String) {
        val count = cacheFailedLogins.getOrDefault(username, 0)
        cacheFailedLogins.put(username, count + 1)
        log.info("The user $username entered wrong password")
        if (cacheFailedLogins[username] == 3) {
            this.suspendUserFollowingBruteForce(username)
        }
    }

    override fun registerSuccessLogin(username: String) {
        cacheFailedLogins.remove(username)
    }

    override fun isNotDeleted(userId: Long): Boolean {
        val user = userDetailsServiceImpl.findById(userId)
        if (user.userState.state == UserStateEntity.UserStateEnum.DELETED) {
            throw UserException(UserExceptionReason.DELETED_USER_IS_NOT_EDITABLE)
        }
        return true
    }

    override fun isNotOnItself(userId: Long, authentication: Authentication): Boolean {
        val user = authentication.principal as JwtUserDetailsImpl
        if (user.id == userId) {
            throw UserException(UserExceptionReason.NOT_ON_YOURSELF)
        }
        return true
    }

    private fun suspendUserFollowingBruteForce(username: String) {
        val user = userDetailsServiceImpl.findByUsername(username)
        user.userState = userStateService.getUserState(UserStateEntity.UserStateEnum.SUSPENDED)
        user.suspensionDate = LocalDateTime.now()
        log.info("The user $username entered wrong password three times and was then suspended")
    }

}