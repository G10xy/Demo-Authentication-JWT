package it.giovi.controller

import it.giovi.errorhandling.exception.UserException
import it.giovi.errorhandling.exception.UserExceptionReason
import it.giovi.model.request.auth.SignInRequest
import it.giovi.model.request.user.UserFirstAccess
import it.giovi.model.response.JwtResponse
import it.giovi.model.response.SuccessResponse
import it.giovi.persistence.entity.UserRoleEntity
import it.giovi.security.JwtUserDetailsImpl
import it.giovi.security.JwtUtils
import it.giovi.security.UserDetailsServiceImpl
import it.giovi.service.UserService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid


@RestController
@RequestMapping("/api/v1/auth")
class AuthControllerImpl(
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: UserDetailsServiceImpl,
    private val jwtUtils: JwtUtils,
    private val userService: UserService
    ) {

    @PostMapping(
        value = ["/signIn"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun userAuthentication(
        @RequestHeader("X-Request-Id") requestId: String,
        @Valid @RequestBody signInRequest: SignInRequest): ResponseEntity<JwtResponse> {
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(signInRequest.username, signInRequest.password)
        )
        SecurityContextHolder.getContext().authentication = authentication
        val jwt = jwtUtils.generateJwtToken(authentication, false)
        val jwtRefresh = jwtUtils.generateJwtToken(authentication, true)
        val userDetails = authentication.principal as JwtUserDetailsImpl
        userService.registerLastSignIn(authentication)
        val role: UserRoleEntity.UserRoleEnum = userDetails.userRole
        return ResponseEntity.ok()
            .body<JwtResponse>(
                JwtResponse(
                    jwt,
                    jwtRefresh,
                    userDetails.username,
                    userDetails.name,
                    userDetails.surname,
                    role,
                    userService.daysBeforeExpiration(userDetails.pwdExpirationDate!!)
                )
            )
    }


    @PostMapping("/refreshToken")
    fun refreshTokenUser(request: HttpServletRequest?): ResponseEntity<JwtResponse> {
        val refreshToken = jwtUtils.parseJwt(request!!)
        return if (refreshToken != null && jwtUtils.validateJwtToken(refreshToken)) {
            val username = jwtUtils.getUserNameFromJwtToken(refreshToken)
            val userDetails: UserDetails = userDetailsService.loadUserByUsername(username)
            val authentication = UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.authorities
            )
            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = authentication
            val refreshedAccessJwt = jwtUtils.generateJwtToken(authentication, false)
            ResponseEntity.ok()
                .body(JwtResponse(accessToken = refreshedAccessJwt, username = userDetails.username))
        } else {
            throw UserException(UserExceptionReason.TOKEN_NOT_REFRESHABLE)
        }
    }


    @PutMapping("/userActivation")
    fun userActivation(@RequestBody userFirstAccess: @Valid UserFirstAccess): ResponseEntity<SuccessResponse> {
        userService.userActivation(userFirstAccess!!)
        return ResponseEntity.ok(SuccessResponse("User correctly activated"))
    }

    @GetMapping("/defaultSecQuestions")
    fun getDefaultSecQuestions(): ResponseEntity<Iterable<String>> {
        return ResponseEntity.ok(userService.findAllDefaultSecurityQuestions())
    }


}