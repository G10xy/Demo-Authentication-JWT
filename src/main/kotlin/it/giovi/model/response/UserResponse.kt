package it.giovi.model.response

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema;
import it.giovi.persistence.entity.UserRoleEntity
import it.giovi.persistence.entity.UserStateEntity
import java.time.LocalDateTime

data class UserResponse(
    @Schema(description = "User Id")
    val id: Long,
    @Schema(description = "Username/mail")
    val username: String,
    @Schema(description = "Name")
    val name: String,
    @Schema(description = "Surname")
    val surname: String,

    @Schema(description = "Creation date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val creationDate: LocalDateTime,

    @Schema(description = "Last activation date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val activationDate: LocalDateTime,

    @Schema(description = "Last suspension date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val suspensionDate: LocalDateTime,

    @Schema(description = "Last modified date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val lastUpdatedDate: LocalDateTime,

    @Schema(description = "User state")
    val userState: UserStateEntity.UserStateEnum,

    @Schema(description = "User role")
    val userRole: UserRoleEntity.UserRoleEnum
)
