package me.chnu.toroid.presentation

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import me.chnu.toroid.ServerException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.ErrorResponse
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.function.RequestPredicates.contentType
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.net.URI
import java.util.*

@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {
    private val kLogger = KotlinLogging.logger {}


    @ExceptionHandler(ServerException::class)
    fun handleServerException(
        ex: ServerException,
        request: HttpServletRequest,
    ): ResponseEntity<ProblemDetail> {
        val status = HttpStatus.valueOf(ex.statusCode)
        val problemDetail = ProblemDetail.forStatus(status).apply {
            contentType(MediaType.APPLICATION_JSON)
            title = status.reasonPhrase
            detail = ex.message
            instance = URI.create(request.requestURI)
        }

        return ResponseEntity.of(problemDetail).build()
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(
        @Suppress("UnusedParameter") ex: AccessDeniedException,
        request: HttpServletRequest,
    ): ResponseEntity<ProblemDetail> {
        val problemDetail = ProblemDetail.forStatus(HttpStatus.FORBIDDEN).apply {
            contentType(MediaType.APPLICATION_JSON)
            title = "Access Denied"
            detail = "You don't have permission to access this resource"
            instance = URI.create(request.requestURI)
        }

        return ResponseEntity.of(problemDetail).build()
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(
        ex: RuntimeException,
        request: HttpServletRequest,
    ): ResponseEntity<ProblemDetail> {
        val problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR).apply {
            contentType(MediaType.APPLICATION_JSON)
            title = "Internal Server Error"
            detail = "Internal server error"
            instance = URI.create(request.requestURI)
        }

        kLogger.error { ex.message }

        return ResponseEntity.of(problemDetail).build()
    }

    override fun handleExceptionInternal(
        ex: Exception,
        body: Any?,
        headers: HttpHeaders,
        statusCode: HttpStatusCode,
        request: WebRequest,
    ): ResponseEntity<Any>? {
        val status = HttpStatus.valueOf(statusCode.value())

        if (ex is ErrorResponse) {
            return ResponseEntity.of(ex.updateAndGetBody(messageSource, Locale.getDefault())).build()
        }

        val problemDetail = ProblemDetail.forStatus(statusCode).apply {
            contentType(MediaType.APPLICATION_JSON)
            title = status.reasonPhrase
            detail = ex.message

            if (request is ServletWebRequest) {
                instance = URI.create(request.request.requestURI)
            }
        }

        return ResponseEntity.of(problemDetail).build()
    }

}
