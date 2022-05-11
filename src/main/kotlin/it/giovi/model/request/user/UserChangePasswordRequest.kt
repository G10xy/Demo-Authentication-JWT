package it.giovi.model.request.user

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;

data class UserChangePasswordRequest(
    @Schema(description = "Password attuale dell'utente", required = true)
    @NotBlank
    val password: String,

    @Schema(description = "Nuova password dell'utente", required = true)
    @NotBlank
    val newPassword: String
)