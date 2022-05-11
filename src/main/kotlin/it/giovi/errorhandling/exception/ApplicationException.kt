package it.giovi.errorhandling.exception

import it.giovi.errorhandling.policy.ApplicationExceptionPolicy
import java.lang.String.format

class ApplicationException : RuntimeException, ApplicationExceptionPolicy {
    override val code: String
    override val message: String

    constructor(reason: ApplicationExceptionReason) {
        this.code = reason.code
        message = reason.message.toString()
    }

    constructor(reason: ApplicationExceptionReason, vararg parameters: Any?) {
        message = if (parameters != null) {
            format(reason.message, parameters)
        } else {
            reason.message.toString()
        }
        this.code = reason.code
    }

    override fun getLocalizedMessage(): String {
        return message
    }

    override fun toString(): String {
        return format("ApplicationException(code=%s, message=%s)", this.code, message)
    }
}
