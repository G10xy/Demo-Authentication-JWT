package it.giovi.model.request.user

import io.swagger.v3.oas.annotations.media.Schema;
import it.giovi.persistence.entity.UserRoleEntity
import javax.validation.constraints.NotBlank;

data class UserInfoChangeRequest(
    @Schema(description = "Nome dell'utente", required = true)
    @NotBlank
    val name: String,

    @Schema(description = "Cognome dell'utente", required = true)
    @NotBlank
    val surname: String,

    @Schema(description = "Ruolo dell'utente", required = true)
    val role: UserRoleEntity.UserRoleEnum
)
