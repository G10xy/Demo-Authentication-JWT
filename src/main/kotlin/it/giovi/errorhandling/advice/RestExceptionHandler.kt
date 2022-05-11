package it.giovi.errorhandling.advice

import it.giovi.errorhandling.exception.ApplicationException
import it.giovi.errorhandling.exception.BusinessException
import it.giovi.model.response.ErrorResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.TypeMismatchException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.*
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.*
import java.util.function.Consumer
import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException
import javax.validation.Path
import kotlin.streams.toList


@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {

    companion object {
        private val log = LoggerFactory.getLogger(RestExceptionHandler::class.java)
    }

    @ExceptionHandler(value = [Exception::class])
    fun handleUncaughtException(ex: Exception, request: ServletWebRequest): ResponseEntity<Any> {
        log(ex, request)
        val errorResponseDto: ErrorResponse = ErrorResponse(
            code = ex.javaClass.simpleName,
            message = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
            status = HttpStatus.INTERNAL_SERVER_ERROR.value())
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDto)
    }

    @ExceptionHandler(BusinessException::class)
    fun handleCustomUncaughtBusinessException(
        ex: BusinessException,
        request: ServletWebRequest
    ): ResponseEntity<Any>? {
        log(ex, request)
        val errorResponseDto: ErrorResponse = ErrorResponse(code = ex.code, message = ex.message, status = ex.httpStatus?.value())
        return ex.httpStatus?.let { ResponseEntity.status(it).body<Any>(errorResponseDto) }
    }

    @ExceptionHandler(ApplicationException::class)
    fun handleCustomUncaughtApplicationException(
        ex: ApplicationException,
        request: ServletWebRequest
    ): ResponseEntity<Any> {
        log(ex, request)
        val errorResponseDto: ErrorResponse = ErrorResponse(code = ex.code, message = ex.message, status = HttpStatus.INTERNAL_SERVER_ERROR.value())
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDto)
    }

    @ExceptionHandler(value = [ConstraintViolationException::class])
    fun handleConstraintViolationException(
        ex: ConstraintViolationException,
        request: ServletWebRequest
    ): ResponseEntity<Any> {
        log(ex, request)
        val invalidParameters: MutableList<ErrorResponse.InvalidParameter> = ArrayList<ErrorResponse.InvalidParameter>()
        ex.constraintViolations.forEach(Consumer { constraintViolation: ConstraintViolation<*> ->
            val it: Iterator<Path.Node> =
                constraintViolation.propertyPath.iterator()
            if (it.hasNext()) {
                try {
                    it.next()
                    val n = it.next()
                    val invalidParameter: ErrorResponse.InvalidParameter = ErrorResponse.InvalidParameter(
                    parameter = n.name,
                    message = constraintViolation.message)
                    invalidParameters.add(invalidParameter)
                } catch (e: Exception) {
                    log.error("[Advocatus] Can't extract the information about constraint violation")
                }
            }
        })
        val errorResponseDto: ErrorResponse = ErrorResponse(
            code = ConstraintViolationException::class.java.simpleName, message = HttpStatus.BAD_REQUEST.reasonPhrase, status = HttpStatus.BAD_REQUEST.value())
        errorResponseDto.invalidParameters

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto)
    }

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders, status: HttpStatus, request: WebRequest
    ): ResponseEntity<Any> {
        log(ex, request as ServletWebRequest)
        val errorResponseDto: ErrorResponse = ErrorResponse(
            code = HttpMessageNotReadableException::class.java.simpleName, message = ex.message.orEmpty(), status = HttpStatus.BAD_REQUEST.value())
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto)
    }

    override fun handleHttpRequestMethodNotSupported(
        ex: HttpRequestMethodNotSupportedException, headers: HttpHeaders, status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        log(ex, request as ServletWebRequest)
        val errorResponseDto: ErrorResponse = ErrorResponse(
            code = HttpRequestMethodNotSupportedException::class.java.simpleName, message = ex.message.orEmpty(), status = HttpStatus.METHOD_NOT_ALLOWED.value())
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponseDto)
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders, status: HttpStatus, request: WebRequest
    ): ResponseEntity<Any> {
        log(ex, request as ServletWebRequest)
        val invalidParameters: List<ErrorResponse.InvalidParameter> = ex.bindingResult.fieldErrors.stream()
            .map{ fieldError: FieldError -> ErrorResponse.InvalidParameter(parameter = fieldError.field, message = "fieldError.defaultMessage")
            }.toList()
        val errorResponseDto: ErrorResponse = ErrorResponse(
            code = MethodArgumentNotValidException::class.java.simpleName, message = HttpStatus.BAD_REQUEST.reasonPhrase, status = HttpStatus.BAD_REQUEST.value())
        errorResponseDto.invalidParameters?.addAll(invalidParameters);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto)
    }

    override fun handleServletRequestBindingException(
        ex: ServletRequestBindingException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        log(ex, request as ServletWebRequest)
        val missingParameter: String
        val missingParameterType: String
        when (ex) {
            is MissingRequestHeaderException -> {
                missingParameter = ex.headerName
                missingParameterType = "header"
            }
            is MissingServletRequestParameterException -> {
                missingParameter = ex.parameterName
                missingParameterType = "query"
            }
            is MissingPathVariableException -> {
                missingParameter = ex.variableName
                missingParameterType = "path"
            }
            else -> {
                missingParameter = "unknown"
                missingParameterType = "unknown"
            }
        }
        val missingParameterDto: ErrorResponse.InvalidParameter = ErrorResponse.InvalidParameter(parameter =  missingParameter, message = String.format("Missing %s parameter with name '%s'", missingParameterType, missingParameter))

        val errorResponseDto: ErrorResponse = ErrorResponse(
            code = ServletRequestBindingException::class.java.simpleName,
            message = HttpStatus.BAD_REQUEST.reasonPhrase, status = HttpStatus.BAD_REQUEST.value())
        errorResponseDto.invalidParameters?.add(missingParameterDto);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto)
    }

    override fun handleTypeMismatch(
        ex: TypeMismatchException, headers: HttpHeaders,
        status: HttpStatus, request: WebRequest
    ): ResponseEntity<Any> {
        log(ex, request as ServletWebRequest)
        var parameter = ex.propertyName
        if (ex is MethodArgumentTypeMismatchException) {
            parameter = ex.name
        }
        val errorResponseDto: ErrorResponse = ErrorResponse(
            code = TypeMismatchException::class.java.simpleName,
            message = String.format("Unexpected type specified for '%s' parameter. Required '%s'", parameter, ex.requiredType),
            status = HttpStatus.BAD_REQUEST.value()
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto)
    }

    override fun handleMissingPathVariable(
        ex: MissingPathVariableException,
        headers: HttpHeaders, status: HttpStatus, request: WebRequest
    ): ResponseEntity<Any> {
        return handleServletRequestBindingException(ex, headers, status, request)
    }

    override fun handleMissingServletRequestParameter(
        ex: MissingServletRequestParameterException, headers: HttpHeaders, status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        log(ex, request as ServletWebRequest)
        return handleServletRequestBindingException(ex, headers, status, request)
    }

    private fun log(ex: Exception, request: ServletWebRequest) {
        val httpMethod: Optional<HttpMethod>
        val requestUrl: Optional<String>
        val possibleIncomingNullRequest = Optional.ofNullable(request)
        if (possibleIncomingNullRequest.isPresent) {
            // get the HTTP Method
            httpMethod = Optional.ofNullable(possibleIncomingNullRequest.get().httpMethod)
            requestUrl = if (Optional.ofNullable(possibleIncomingNullRequest.get().request).isPresent) {
                // get the Request URL
                Optional.of(possibleIncomingNullRequest.get().request.requestURL.toString())
            } else {
                Optional.empty()
            }
        } else {
            httpMethod = Optional.empty()
            requestUrl = Optional.empty()
        }
        log.error(
            "Request {} {} failed with exception reason: {}",
            if (httpMethod.isPresent) httpMethod.get() else "'null'",
            requestUrl.orElse("'null'"), ex.message, ex
        )
    }
}
