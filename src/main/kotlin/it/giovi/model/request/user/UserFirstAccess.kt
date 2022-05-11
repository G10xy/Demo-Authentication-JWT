package it.giovi.model.request.user

import io.swagger.v3.oas.annotations.media.Schema
import it.giovi.annotation.ValidPassword
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class UserFirstAccess(
    @Email
    @Schema(description = "Username dell'utente", required = true)
    @NotBlank
    val username:String,

    @Schema(description = "Nuova password dell'utente", required = true)
    @ValidPassword
    val password: String,

    @Schema(description = "La password di solo primo ed unico accesso", required = true)
    @NotBlank
    val otp: String,

    @Schema(description = "Domanda segreta dell'utente", required = true)
    @NotBlank
    val question: String,

    @Schema(description = "Risposta segreta dell'utente", required = true)
    @NotBlank
    val answer: String
)
