package it.giovi.model.request.user

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;

data class UserLostPasswordRequest(

    @Schema(description = "Secret question", required = true)
    @NotBlank
    val question: String,

    @Schema(description = "Secret answer", required = true)
    @NotBlank
    val answer: String
)
