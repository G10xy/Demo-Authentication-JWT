package it.giovi.model.response

import io.swagger.v3.oas.annotations.media.Schema
import it.giovi.persistence.entity.UserRoleEntity

data class JwtResponse @JvmOverloads constructor (
    @Schema(description = "Access Token")
    val accessToken: String,
    @Schema(description = "Token to be used if you want refresh access token")
    val refreshToken: String? = null,
    @Schema(description = "Username")
    val username: String,
    @Schema(description = "Name")
    val name: String? = null,
    @Schema(description = "Surname")
    val surname: String? = null,
    @Schema(description = "Role")
    val role: UserRoleEntity.UserRoleEnum? = null,
    @Schema(description = "Days before password expiration")
    val daysBeforeExpiration: Long? = null)
