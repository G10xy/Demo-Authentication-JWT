package it.giovi.model.response

import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable


data class SuccessResponse(
    @Schema(description = "The success message")
    private val message: String? = null
){
}
