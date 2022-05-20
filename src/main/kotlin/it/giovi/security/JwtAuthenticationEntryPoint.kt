package it.giovi.security

import com.fasterxml.jackson.databind.ObjectMapper
import it.giovi.model.response.ErrorResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint {


    private val log = LoggerFactory.getLogger(JwtAuthenticationEntryPoint::class.java)

    @Throws(IOException::class)
    override fun commence(
        request: HttpServletRequest?, response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        log.error("Unauthorized error: {}", authException.message)
        response.contentType = "application/json"
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        val responseBody = ObjectMapper().writeValueAsString(
            ErrorResponse(
                authException.javaClass.simpleName,
                authException.message.orEmpty(),
                HttpStatus.UNAUTHORIZED.value()
            )
        )
        response.outputStream.println(responseBody)
    }
}
