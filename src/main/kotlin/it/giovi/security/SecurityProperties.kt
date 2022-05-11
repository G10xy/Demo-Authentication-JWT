package it.giovi.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

import javax.xml.crypto.Data


@Component
@ConfigurationProperties(prefix = "security")
class SecurityProperties(
    val jwtSecret: String,
    val jwtExpirationMs: Long,
    val jwtRefreshExpirationMs: Long,
    val defaultQuestionFile: String
) {
}
