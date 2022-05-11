package it.giovi.errorhandling.exception

import org.springframework.http.HttpStatus


class UserException : BusinessException {

    constructor(reason: UserExceptionReason) : super(reason) {}
    constructor(reason: UserExceptionReason, overridingHttpStatus: HttpStatus?) : super(
        reason,
        overridingHttpStatus
    ) {
    }

    constructor(reason: UserExceptionReason, vararg parameters: Any?) : super(reason, parameters) {}
}
