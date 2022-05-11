package it.giovi.errorhandling.policy

import org.springframework.http.HttpStatus


interface BusinessExceptionPolicy : ExceptionPolicy {
    val httpStatus: HttpStatus?
}
