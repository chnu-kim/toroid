package me.chnu.toroid.presentation

import me.chnu.toroid.ServerException
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.function.RequestPredicates.contentType

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ServerException::class)
    fun handleServerException(ex: ServerException): ResponseEntity<ProblemDetail> {
        val status = HttpStatus.valueOf(ex.statusCode)
        val problemDetail = ProblemDetail.forStatus(status).apply {
            contentType(MediaType.APPLICATION_JSON)
            detail = ex.message
            title = status.reasonPhrase
        }

        return ResponseEntity.of(problemDetail).build()
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(ex: RuntimeException): ResponseEntity<ProblemDetail> {
        val problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR).apply {
            contentType(MediaType.APPLICATION_JSON)
            detail = "Internal server error"
            title = "Internal Server Error"
        }

        return ResponseEntity.of(problemDetail).build()
    }

}