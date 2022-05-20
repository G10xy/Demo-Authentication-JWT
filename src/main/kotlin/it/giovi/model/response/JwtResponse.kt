package it.giovi.model.response

import it.giovi.persistence.entity.UserRoleEntity

data class JwtResponse @JvmOverloads constructor (
    val accessToken: String,
    val refreshToken: String? = null,
    val username: String,
    val name: String? = null,
    val surname: String? = null,
    val role: UserRoleEntity.UserRoleEnum? = null,
    val daysBeforeExpiration: Long? = null)
