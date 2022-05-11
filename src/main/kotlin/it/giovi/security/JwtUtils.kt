package it.giovi.security

import io.jsonwebtoken.*;
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import java.security.SignatureException
import java.util.*
import javax.servlet.http.HttpServletRequest


@Component
class JwtUtils(
    private val securityProperties: SecurityProperties
) {

    companion object {
        private val log = LoggerFactory.getLogger(JwtUtils::class.java)
    }

    fun generateJwtToken(authentication: Authentication, refresh: Boolean): String {
        val userPrincipal: JwtUserDetailsImpl = authentication.getPrincipal() as JwtUserDetailsImpl
        val expirationMs: Long =
            if (refresh) securityProperties.jwtRefreshExpirationMs else securityProperties.jwtExpirationMs
        return Jwts.builder()
            .setSubject(userPrincipal.username)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + expirationMs))
            .signWith(SignatureAlgorithm.HS512, securityProperties.jwtSecret)
            .compact()
    }

    fun getUserNameFromJwtToken(token: String?): String {
        return Jwts.parser().setSigningKey(securityProperties.jwtSecret).parseClaimsJws(token).getBody()
            .getSubject()
    }

    fun validateJwtToken(authToken: String?): Boolean {
        try {
            Jwts.parser().setSigningKey(securityProperties.jwtSecret).parseClaimsJws(authToken)
            return true
        } catch (e: SignatureException) {
            log.error("Invalid JWT signature: {}", e.message)
        } catch (e: MalformedJwtException) {
            log.error("Invalid JWT token: {}", e.message)
        } catch (e: ExpiredJwtException) {
            log.error("JWT token is expired: {}", e.message)
        } catch (e: UnsupportedJwtException) {
            log.error("JWT token is unsupported: {}", e.message)
        } catch (e: IllegalArgumentException) {
            log.error("JWT claims string is empty: {}", e.message)
        }
        return false
    }

    fun parseJwt(request: HttpServletRequest): String? {
        val headerAuth: String = request.getHeader("Authorization")
        return if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            headerAuth.substring(7)
        } else null
    }
}
