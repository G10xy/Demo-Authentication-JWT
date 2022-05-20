package it.giovi.model.response

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime


data class ErrorResponse(
    @Schema(description = "Exception type") private val code: String?,
    @Schema(description = "Exception message") private val message: String?,
    @Schema(description = "Exception code") private val status: Int?
) {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private val timestamp = LocalDateTime.now()
    var invalidParameters: MutableList<InvalidParameter>? = null

    data class InvalidParameter(val parameter: String, val message: String) {}
}

