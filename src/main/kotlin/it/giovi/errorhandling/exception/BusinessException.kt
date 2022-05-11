package it.giovi.errorhandling.exception

import it.giovi.errorhandling.policy.BusinessExceptionPolicy
import org.springframework.http.HttpStatus
import java.lang.String.format


open class BusinessException : RuntimeException, BusinessExceptionPolicy {
    override val code: String?
    override val message: String?
    override val httpStatus: HttpStatus?

    constructor(reason: BusinessExceptionPolicy) {
        this.code = reason.code
        message = reason.message
        httpStatus = reason.httpStatus
    }

    constructor(reason: BusinessExceptionPolicy, overridingHttpStatus: HttpStatus) {
        this.code = reason.code
        message = reason.message
        httpStatus = overridingHttpStatus
    }

    constructor(reason: BusinessExceptionPolicy, vararg parameters: Any?) {
        message = if (parameters != null) {
            format(reason.message, parameters)
        } else {
            reason.message
        }
        this.code = reason.code
        httpStatus = reason.httpStatus
    }

    override fun getLocalizedMessage(): String? {
        return message
    }

    override fun toString(): String {
        return format(
            "BusinessException(code=%s, message=%s, httpStatus=%s)", this.code, message,
            this.httpStatus?.value()
        )
    }
}
