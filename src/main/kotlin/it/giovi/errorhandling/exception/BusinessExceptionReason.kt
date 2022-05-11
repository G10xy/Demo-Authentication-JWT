package it.giovi.errorhandling.exception

import it.giovi.errorhandling.policy.BusinessExceptionPolicy
import org.springframework.http.HttpStatus


enum class BusinessExceptionReason(override val message: String, override val httpStatus: HttpStatus) : BusinessExceptionPolicy {
    REQUEST_ERROR("Invalid input parameters", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND("No such user role was found.", HttpStatus.NOT_FOUND),
    STATE_NOT_FOUND("No such user status was found.", HttpStatus.NOT_FOUND);


    override val code: String = BusinessExceptionReason::class.java.simpleName
}
