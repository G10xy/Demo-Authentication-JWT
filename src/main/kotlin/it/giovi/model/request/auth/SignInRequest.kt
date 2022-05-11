package it.giovi.model.request.auth

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotBlank

data class SignInRequest(
    @Schema(description = "Username dell'utente", required = true)
    @NotBlank(message = "Username is mandatory")
    val username: String,

    @Schema(description = "Password dell'utente", required = true)
    @NotBlank(message = "Password is mandatory")
    val password: String
)
