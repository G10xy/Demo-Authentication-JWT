package it.giovi.model.request.auth

import io.swagger.v3.oas.annotations.media.Schema;
import it.giovi.persistence.entity.UserRoleEntity
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size

data class SignUpRequest(
    @Schema(description = "Nome dell'utente", required = true)
    @NotBlank
    val name: String,

    @Schema(description = "Cognome dell'utente", required = true)
    @NotBlank
    val surname: String,

    @Schema(description = "Mail dell'utente", required = true)
    @NotBlank
    @Email
    @Size(max = 100, message = "Username is too long")
    val username: String,

    @Schema(description = "Profilo dell'utente", required = true)
    val role: UserRoleEntity.UserRoleEnum
)
