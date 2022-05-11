package it.giovi.model.response

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime


data class ErrorResponse(
    @Schema(description = "Il tipo di eccezione") private val code: String?,
    @Schema(description = "Il messaggio di errore") private val message: String?,
    @Schema(description = "Il codice dell'errore") private val status: Int?
) {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private val timestamp = LocalDateTime.now()
    var invalidParameters: MutableList<InvalidParameter>? = null

    data class InvalidParameter(val parameter: String, val message: String) {}
}

