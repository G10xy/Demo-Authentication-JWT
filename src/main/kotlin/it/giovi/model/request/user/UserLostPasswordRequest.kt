package it.giovi.model.request.user

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;

data class UserLostPasswordRequest(

    @Schema(description = "Vecchia password dell'utente", required = true)
    @NotBlank
    val question: String,

    @Schema(description = "Nuova password dell'utente", required = true)
    @NotBlank
    val answer: String
)
