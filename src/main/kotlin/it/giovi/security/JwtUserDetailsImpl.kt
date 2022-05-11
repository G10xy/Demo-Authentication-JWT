package it.giovi.security

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import it.giovi.persistence.entity.UserRoleEntity
import it.giovi.persistence.entity.UserStateEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime


@JsonIgnoreProperties(value = ["accountNonExpired", "accountNonLocked", "credentialsNonExpired"])
class JwtUserDetailsImpl(

    val id: Long?,
    private val username: String,
    @field:JsonIgnore
    private val password: String?,
    val name: String,
    val surname: String,
    val userState: UserStateEntity.UserStateEnum,
    val userRole: UserRoleEntity.UserRoleEnum,
    val pwdExpirationDate: LocalDateTime?,
    private val authority: GrantedAuthority
) :
    UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(authority)
    }

    override fun getPassword(): String? {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
