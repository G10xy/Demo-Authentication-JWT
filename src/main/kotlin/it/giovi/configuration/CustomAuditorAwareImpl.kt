package it.giovi.configuration

import it.giovi.security.JwtUserDetailsImpl
import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*
import org.springframework.security.core.Authentication;

import java.util.Optional;

class CustomAuditorAwareImpl : AuditorAware<String> {
    override fun getCurrentAuditor(): Optional<String> {
        val authentication: Authentication? = SecurityContextHolder.getContext().authentication
        return if ((authentication == null) || !authentication.isAuthenticated || authentication.principal.equals("anonymousUser")) {
            Optional.empty()
        } else Optional.of((authentication.principal as JwtUserDetailsImpl).username)
    }
}