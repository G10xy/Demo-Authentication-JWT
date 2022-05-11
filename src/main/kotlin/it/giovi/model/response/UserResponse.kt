package it.giovi.model.response

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema;
import it.giovi.persistence.entity.UserRoleEntity
import it.giovi.persistence.entity.UserStateEntity
import java.time.LocalDateTime

data class UserResponse(
    @Schema(description = "User Id")
    val id: Long,
    @Schema(description = "Indirizzo email")
    val username: String,
    @Schema(description = "Nome utente")
    val name: String,
    @Schema(description = "Cognome utente")
    val surname: String,

    @Schema(description = "Data di creazione")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val creationDate: LocalDateTime,

    @Schema(description = "Data di ultima attivazione")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val activationDate: LocalDateTime,

    @Schema(description = "Data di ultima sospensione")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val suspensionDate: LocalDateTime,

    @Schema(description = "Data di cancellazione logica")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val disableDate: LocalDateTime,

    @Schema(description = "Data di ultima modifica")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val lastUpdatedDate: LocalDateTime,

    @Schema(description = "Stato utente")
    val userState: UserStateEntity.UserStateEnum,

    @Schema(description = "Ruolo del profilo utente")
    val userRole: UserRoleEntity.UserRoleEnum
)
