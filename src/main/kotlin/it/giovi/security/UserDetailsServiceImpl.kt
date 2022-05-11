package it.giovi.security

import it.giovi.errorhandling.exception.UserException
import it.giovi.errorhandling.exception.UserExceptionReason
import it.giovi.persistence.UserSpecifications
import it.giovi.persistence.entity.UserEntity
import it.giovi.persistence.entity.UserStateEntity.UserStateEnum
import it.giovi.persistence.repository.UserRepository
import it.giovi.security.JwtUserDetailsFactory.create
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.function.Supplier


@Service
class UserDetailsServiceImpl(private val userRepository: UserRepository) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user: UserEntity = userRepository.findOne(
            UserSpecifications.userByUsername(username).and(UserSpecifications.userByState(UserStateEnum.ACTIVE))
        )
            .orElseThrow<RuntimeException>(Supplier<RuntimeException> { UserException(UserExceptionReason.USERNAME_NOT_FOUND_FOR_ENABLED_USER) })
        return create(user)
    }

    fun findById(userId: Long): UserEntity {
        return userRepository.findById(userId)
            .orElseThrow<RuntimeException> {
                throw UserException(
                    UserExceptionReason.USER_ENTITY_NOT_FOUND
                )
            }
    }

    fun findByUsername(username: String): UserEntity {
        return userRepository.findOne(UserSpecifications.userByUsername(username).and(UserSpecifications.userByState(UserStateEnum.ACTIVE)))
            .orElseThrow<RuntimeException> {
                throw UserException(UserExceptionReason.USER_ENTITY_NOT_FOUND)
            }
    }
}