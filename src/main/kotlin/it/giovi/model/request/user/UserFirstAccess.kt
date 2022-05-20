package it.giovi.model.request.user

import io.swagger.v3.oas.annotations.media.Schema
import it.giovi.annotation.ValidPassword
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class UserFirstAccess(
    @Email
    @Schema(description = "Username", required = true)
    @NotBlank
    val username:String,

    @Schema(description = "New password", required = true)
    @ValidPassword
    val password: String,

    @Schema(description = "OTP password", required = true)
    @NotBlank
    val otp: String,

    @Schema(description = "Secret question", required = true)
    @NotBlank
    val question: String,

    @Schema(description = "Secret answer", required = true)
    @NotBlank
    val answer: String
)
