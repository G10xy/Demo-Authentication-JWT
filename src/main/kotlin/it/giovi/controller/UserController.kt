package it.giovi.controller

import io.swagger.v3.oas.annotations.enums.ParameterIn
import it.giovi.model.request.auth.SignUpRequest
import it.giovi.model.request.user.UserChangePasswordRequest
import it.giovi.model.request.user.UserInfoChangeRequest
import it.giovi.model.request.user.UserLostPasswordRequest
import it.giovi.model.response.ErrorResponse
import it.giovi.model.response.SuccessResponse
import it.giovi.model.response.UserResponse
import it.giovi.persistence.entity.UserRoleEntity.UserRoleEnum
import it.giovi.persistence.entity.UserStateEntity.UserStateEnum
import org.springframework.http.ResponseEntity
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import org.springframework.web.bind.annotation.RequestHeader
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.RequestParam
import javax.servlet.http.HttpServletRequest
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PathVariable
import javax.validation.Valid


interface UserController {
    @Operation(
        operationId = "getUser",
        summary = "User Registration",
        description = "API allowing to register a user",
        tags = ["User"],
        parameters = [Parameter(
            `in` = ParameterIn.PATH,
            name = "id",
            description = "User Id",
            schema = Schema(type = "long")
        )],
        responses = [ApiResponse(
            responseCode = "201",
            description = "Success",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = UserResponse::class))]
        ), ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        )]
    )
    fun getUser(
        @RequestHeader("X-Request-Id") requestId: String,
        @PathVariable("id") id: Long
    ): ResponseEntity<UserResponse>

    @Operation(
        operationId = "registerUser",
        summary = "User Registration",
        description = "API for user registration",
        tags = ["User"],
        requestBody = RequestBody(
            description = "Model with user data",
            required = true,
            content = [Content(
                schema = Schema(implementation = SignUpRequest::class),
                mediaType = MediaType.APPLICATION_JSON_VALUE
            )]
        ),
        responses = [ApiResponse(
            responseCode = "201",
            description = "Success",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = SuccessResponse::class))]
        ), ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "403",
            description = "Forbidden",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "406",
            description = "Not Acceptable",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        )]
    )
    fun registerUser(
        @RequestHeader("X-Request-Id") requestId: String,
        @RequestBody dtoRequest: SignUpRequest,
        authentication: Authentication
    ): ResponseEntity<SuccessResponse>

    @Operation(
        operationId = "getUsersByFiltering",
        summary = "Users list from advanced search",
        description = "API responding with the list of users following the input of search parameters. The list contains only users of the same role and lower level than the user making the request.",
        tags = ["User"],
        parameters = [Parameter(
            `in` = ParameterIn.QUERY,
            name = "username",
            description = "Email",
            schema = Schema(type = "string")
        ), Parameter(
            `in` = ParameterIn.QUERY,
            name = "name",
            description = "Name",
            schema = Schema(type = "string")
        ), Parameter(
            `in` = ParameterIn.QUERY,
            name = "surname",
            description = "Surname",
            schema = Schema(type = "string")
        ), Parameter(
            `in` = ParameterIn.QUERY,
            name = "role",
            description = "User role",
            schema = Schema(type = "string")
        ), Parameter(
            `in` = ParameterIn.QUERY,
            name = "state",
            description = "User state",
            schema = Schema(type = "string")
        )],
        responses = [ApiResponse(
            responseCode = "200",
            description = "Success",
            content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = Schema(type = "array", implementation = SuccessResponse::class)
            )]
        ), ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "403",
            description = "Forbidden",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "406",
            description = "Not Acceptable",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        )]
    )
    fun getUsersByFiltering(
        @RequestHeader("X-Request-Id") requestId: String,
        @RequestParam username: String,
        @RequestParam name: String,
        @RequestParam surname: String,
        @RequestParam role: UserRoleEnum,
        @RequestParam state: UserStateEnum
    ): Iterable<UserResponse>

    @Operation(
        operationId = "modifyPassword",
        summary = "Modify Password",
        description = "API to modify logged-in user password",
        tags = ["User"],
        requestBody = RequestBody(
            description = "Model with current password and new desired password for authentication",
            required = true,
            content = [Content(
                schema = Schema(implementation = UserChangePasswordRequest::class),
                mediaType = MediaType.APPLICATION_JSON_VALUE
            )]
        ),
        responses = [ApiResponse(
            responseCode = "200",
            description = "Success",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = SuccessResponse::class))]
        ), ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "403",
            description = "Forbidden",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "406",
            description = "Not Acceptable",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        )]
    )
    fun modifyPassword(
        @RequestHeader("X-Request-Id") requestId: String,
        @RequestBody dtoRequest: UserChangePasswordRequest,
        authentication: Authentication
    ): ResponseEntity<SuccessResponse>

    @Operation(
        operationId = "modifyUser",
        summary = "Edit user information",
        description = "API to edit the basic information and role of a user",
        tags = ["User"],
        parameters = [Parameter(
            `in` = ParameterIn.PATH,
            name = "id",
            description = "Id of the user whose changes you want to make",
            schema = Schema(type = "long")
        )],
        requestBody = RequestBody(
            description = "Model containing modified first name, last name, and role of the user",
            required = true,
            content = [Content(
                schema = Schema(implementation = UserInfoChangeRequest::class),
                mediaType = MediaType.APPLICATION_JSON_VALUE
            )]
        ),
        responses = [ApiResponse(
            responseCode = "200",
            description = "Success",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = SuccessResponse::class))]
        ), ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "403",
            description = "Forbidden",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "406",
            description = "Not Acceptable",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        )]
    )
    fun modifyUser(
        @RequestHeader("X-Request-Id") requestId: String,
        @PathVariable("id") id: Long,
        @RequestBody dtoRequest: UserInfoChangeRequest, authentication: Authentication
    ): ResponseEntity<SuccessResponse>

    @Operation(
        operationId = "disableUser",
        summary = "User Suspension",
        description = "API to suspend a user",
        tags = ["User"],
        parameters = [Parameter(
            `in` = ParameterIn.PATH,
            name = "id",
            description = "Id dell'utente che si vuole disabilitare",
            schema = Schema(type = "long")
        )],
        responses = [ApiResponse(
            responseCode = "200",
            description = "Success",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = SuccessResponse::class))]
        ), ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "403",
            description = "Forbidden",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "406",
            description = "Not Acceptable",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        )]
    )
    fun disableUser(
        @RequestHeader("X-Request-Id") requestId: String,
        @PathVariable("id") id: Long,
        authentication: Authentication
    ): ResponseEntity<SuccessResponse>

    @Operation(
        operationId = "enableUser",
        summary = "User Enabling",
        description = "API to enable user",
        tags = ["User"],
        parameters = [Parameter(
            `in` = ParameterIn.PATH,
            name = "id",
            description = "Id of the user to be enabled",
            schema = Schema(type = "long")
        )],
        responses = [ApiResponse(
            responseCode = "200",
            description = "Success",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = SuccessResponse::class))]
        ), ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "403",
            description = "Forbidden",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "406",
            description = "Not Acceptable",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        )]
    )
    fun enableUser(
        @RequestHeader("X-Request-Id") requestId: String,
        @PathVariable("id") id: Long,
        authentication: Authentication
    ): ResponseEntity<SuccessResponse>

    @Operation(
        operationId = "lostPassword",
        summary = "Password recreation request",
        description = "API to request a temporary password and perform a new user creation procedure",
        tags = ["User"],
        requestBody = RequestBody(
            description = "Model containing user security question and answer",
            required = true,
            content = [Content(
                schema = Schema(implementation = UserLostPasswordRequest::class),
                mediaType = MediaType.APPLICATION_JSON_VALUE
            )]
        ),
        responses = [ApiResponse(
            responseCode = "200",
            description = "Success",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = SuccessResponse::class))]
        ), ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "403",
            description = "Forbidden",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "406",
            description = "Not Acceptable",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        )]
    )
    fun lostPassword(
        @RequestHeader("X-Request-Id") requestId: String,
        @RequestBody dtoRequest: UserLostPasswordRequest,
        authentication: Authentication
    ): ResponseEntity<SuccessResponse>

    @Operation(
        operationId = "resetUserPassword",
        summary = "User Reset Password",
        description = "API to reset a user password",
        tags = ["User"],
        parameters = [Parameter(
            `in` = ParameterIn.PATH,
            name = "id",
            description = "Id of the user whose password is to be reset",
            schema = Schema(type = "long")
        )],
        responses = [ApiResponse(
            responseCode = "200",
            description = "Success",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = SuccessResponse::class))]
        ), ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "403",
            description = "Forbidden",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "406",
            description = "Not Acceptable",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        )]
    )
    fun resetUserPassword(
        @RequestHeader("X-Request-Id") requestId: String,
        @PathVariable("id") id: Long,
        authentication: Authentication
    ): ResponseEntity<SuccessResponse>

    @Operation(
        operationId = "deleteUser",
        summary = "User Deletion",
        description = "API to remove a user",
        tags = ["User"],
        parameters = [Parameter(
            `in` = ParameterIn.PATH,
            name = "id",
            description = "Id of the user to be deleted",
            schema = Schema(type = "long")
        )],
        responses = [ApiResponse(
            responseCode = "200",
            description = "Success",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = SuccessResponse::class))]
        ), ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "403",
            description = "Forbidden",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "406",
            description = "Not Acceptable",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        )]
    )
    fun deleteUser(
        @RequestHeader("X-Request-Id") requestId: String,
        @PathVariable("id") id: Long,
        authentication: Authentication
    ): ResponseEntity<SuccessResponse>

    @Operation(
        operationId = "userSignOut",
        summary = "User Logout",
        description = "API to logout",
        tags = ["User"],
        responses = [ApiResponse(
            responseCode = "200",
            description = "Success",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = SuccessResponse::class))]
        ), ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        )]
    )
    fun userSignOut(
        @RequestHeader("X-Request-Id") requestId: String,
        request: HttpServletRequest,
        authentication: Authentication
    ): ResponseEntity<SuccessResponse>

    @Operation(
        operationId = "getRoles",
        summary = "User Roles List",
        description = "API to retrieve the list of role a user can have",
        tags = ["User"],
        responses = [ApiResponse(
            responseCode = "200",
            description = "Success",
            content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = Schema(type = "array", implementation = String::class)
            )]
        ), ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "403",
            description = "Forbidden",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        )]
    )
    fun getRoles(@RequestHeader("X-Request-Id") requestId: String): Iterable<String>

    @Operation(
        operationId = "getStates",
        summary = "User States List",
        description = "API to retrieve the list of states in which a user may be in",
        tags = ["User"],
        responses = [ApiResponse(
            responseCode = "200",
            description = "Success",
            content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = Schema(type = "array", implementation = String::class)
            )]
        ), ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "403",
            description = "Forbidden",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        )]
    )
    fun getStates(@RequestHeader("X-Request-Id") requestId: String): Iterable<String>
}
