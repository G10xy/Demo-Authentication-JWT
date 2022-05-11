package it.giovi.security

import it.giovi.persistence.entity.UserEntity
import org.springframework.security.core.authority.SimpleGrantedAuthority


object JwtUserDetailsFactory {
    fun create(user: UserEntity): JwtUserDetailsImpl {
        return JwtUserDetailsImpl(
            user.id,
            user.username,
            user.password,
            user.name,
            user.surname,
            user.userState.state,
            user.userRole.role,
            user.pwdExpirationDate,
            SimpleGrantedAuthority(user.userRole.role.name)
        )
    }
}
