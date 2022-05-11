package it.giovi.errorhandling.exception

import it.giovi.errorhandling.policy.BusinessExceptionPolicy
import org.springframework.http.HttpStatus


enum class UserExceptionReason(override val message: String, override val httpStatus: HttpStatus) : BusinessExceptionPolicy {
    DELETED_USER_IS_NOT_EDITABLE(
        "The wanted user has been deleted and is therefore no longer editable",
        HttpStatus.METHOD_NOT_ALLOWED
    ),
    USER_ALREADY_SUSPENDED(
        "The wanted user has already been suspended",
        HttpStatus.METHOD_NOT_ALLOWED
    ),
    USER_ALREADY_DELETED(
        "The wanted user has already been deleted",
        HttpStatus.METHOD_NOT_ALLOWED
    ),
    USER_ALREADY_ENABLED(
        "The wanted user has already been enabled",
        HttpStatus.METHOD_NOT_ALLOWED
    ),
    USER_ENTITY_NOT_FOUND(
        "The wanted user has not been found",
        HttpStatus.NOT_FOUND
    ),
    USERNAME_NOT_VALID(
        "The username is invalid",
        HttpStatus.UNAUTHORIZED
    ),
    PASSWORD_NOT_VALID(
        "The password is not valid",
        HttpStatus.UNAUTHORIZED
    ),
    PASSWORD_BANNED(
        "The password is banned since it has been among the banned ones",
        HttpStatus.METHOD_NOT_ALLOWED
    ),
    OTP_EXPIRED(
        "The temporary password is expired",
        HttpStatus.FORBIDDEN
    ),
    OTP_NOT_VALID(
        "The temporary password is not valid",
        HttpStatus.UNAUTHORIZED
    ),
    USERNAME_ALREADY_ASSIGNED(
        "The username you entered is already assigned to an enabled user",
        HttpStatus.METHOD_NOT_ALLOWED
    ),
    FORBIDDEN_EXCEPTION(
        "Your profile has a role that does not allow you to perform this required action on the indicated user",
        HttpStatus.FORBIDDEN
    ),
    NOT_ON_YOURSELF(
        "The operation is not allowed on own account",
        HttpStatus.METHOD_NOT_ALLOWED
    ),
    SEC_QUESTION_WRONG(
        "The security question is wrong",
        HttpStatus.UNAUTHORIZED
    ),
    SEC_ANSWER_WRONG(
        "The security answer is wrong",
        HttpStatus.UNAUTHORIZED
    ),
    TOKEN_NOT_REFRESHABLE(
        "The authentication token is not refreshable",
        HttpStatus.METHOD_NOT_ALLOWED
    ),
    USERNAME_NOT_FOUND_FOR_ENABLED_USER(
        "There is no enabled user with the entered username",
        HttpStatus.NOT_FOUND
    ),
    NULL_CREDENTIALS("Authentication failed because no credentials were entered", HttpStatus.BAD_REQUEST);

    override val code: String = UserExceptionReason::class.java.simpleName
}
