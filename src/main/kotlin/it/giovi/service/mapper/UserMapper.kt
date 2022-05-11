package it.giovi.service.mapper

import it.giovi.model.response.UserResponse
import it.giovi.persistence.entity.UserEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings


@Mapper(componentModel = "spring")
interface UserMapper {

    @Mappings(
        *[Mapping(
            target = "userState",
            expression = "java(user.getUserState().getState())"
        ), Mapping(target = "userRole", expression = "java(user.getUserRole().getRole())")]
    )
    fun userEntityToUserResponse(user: UserEntity): UserResponse
}