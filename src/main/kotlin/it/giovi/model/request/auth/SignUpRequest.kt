package it.giovi.model.request.auth

import io.swagger.v3.oas.annotations.media.Schema;
import it.giovi.persistence.entity.UserRoleEntity
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size

data class SignUpRequest(
    @Schema(description = "Name", required = true)
    @NotBlank
    val name: String,

    @Schema(description = "Surname", required = true)
    @NotBlank
    val surname: String,

    @Schema(description = "Username", required = true)
    @NotBlank
    @Email
    @Size(max = 100, message = "Username is too long")
    val username: String,

    @Schema(description = "Role", required = true)
    val role: UserRoleEntity.UserRoleEnum
)
