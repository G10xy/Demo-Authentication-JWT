package it.giovi.model.request.auth

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotBlank

data class SignInRequest(
    @Schema(description = "Username", required = true)
    @NotBlank
    val username: String,

    @Schema(description = "Password", required = true)
    @NotBlank
    val password: String
)
