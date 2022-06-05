package it.giovi.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import it.giovi.model.request.auth.SignInRequest
import it.giovi.model.request.user.UserFirstAccess
import it.giovi.model.response.ErrorResponse
import it.giovi.model.response.JwtResponse
import it.giovi.model.response.SuccessResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestHeader
import javax.servlet.http.HttpServletRequest


interface AuthController {

    @Operation(
        operationId = "userAuthentication",
        summary = "User Authentication",
        description = "API responding to user authentication request with user data and the JWT token",
        tags = ["Authentication"],
        requestBody = RequestBody(
            description = "Model with username and password required for authentication",
            required = true,
            content = [Content(
                schema = Schema(implementation = SignInRequest::class),
                mediaType = MediaType.APPLICATION_JSON_VALUE
            )]
        ),
        responses = [ApiResponse(
            responseCode = "200",
            description = "Success",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = JwtResponse::class))]
        ), ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "404",
            description = "Not Found",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        )]
    )
    fun userAuthentication(
        @RequestHeader("X-Request-Id") requestId: String,
        @RequestBody signInRequest: SignInRequest
    ): ResponseEntity<JwtResponse>

    @Operation(
        operationId = "refreshTokenUser",
        summary = "Refresh Token",
        description = "API to update the authentication token",
        tags = ["Authentication"],
        parameters = [Parameter(
            `in` = ParameterIn.HEADER,
            name = "Bearer ",
            description = "JWT expired",
            schema = Schema(type = "string")
        )],
        responses = [ApiResponse(
            responseCode = "200",
            description = "Success",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = JwtResponse::class))]
        ), ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        )]
    )
    fun refreshTokenUser(
        @RequestHeader("X-Request-Id") requestId: String,
        request: HttpServletRequest
    ): ResponseEntity<JwtResponse>

    @Operation(
        operationId = "userActivation",
        summary = "User Activation",
        description = "API that allows a user's first login for subsequent activation",
        tags = ["Authentication"],
        requestBody = RequestBody(
            description = "Model with username, otp, password, question and security response for authentication",
            required = true,
            content = [Content(
                schema = Schema(implementation = UserFirstAccess::class),
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
            responseCode = "404",
            description = "Not Found",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        ), ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorResponse::class))]
        )]
    )
    fun userActivation(
        @RequestHeader("X-Request-Id") requestId: String,
        @RequestBody userFirstAccess: UserFirstAccess
    ): ResponseEntity<SuccessResponse>

    @Operation(
        operationId = "getDefaultSecQuestions",
        summary = "Default Security Questions",
        description = "API responding with a list of predefined security questions",
        tags = ["Authentication"],
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
    fun getDefaultSecQuestions(@RequestHeader("X-Request-Id") requestId: String): ResponseEntity<Iterable<String>>
}
