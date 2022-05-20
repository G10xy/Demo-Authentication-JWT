package it.giovi.model.request.user

import io.swagger.v3.oas.annotations.media.Schema;
import it.giovi.persistence.entity.UserRoleEntity
import javax.validation.constraints.NotBlank;

data class UserInfoChangeRequest(
    @Schema(description = "Name", required = true)
    @NotBlank
    val name: String,

    @Schema(description = "Surname", required = true)
    @NotBlank
    val surname: String,

    @Schema(description = "Role", required = true)
    val role: UserRoleEntity.UserRoleEnum
)
