package it.giovi.service

import org.springframework.security.core.Authentication

interface UserSecurityService {

    fun registerFailedLogin(username: String)
    fun registerSuccessLogin(username: String)
    fun isNotOnItself(userId: Long, authentication: Authentication): Boolean

}