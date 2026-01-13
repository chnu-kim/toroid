package me.chnu.chzzk.presentation

import me.chnu.chzzk.domain.auth.ChzzkAuthService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class ChzzkAuthController(
    private val chzzkAuthService: ChzzkAuthService,
) {

    @GetMapping("/chzzk/authentication")
    fun authenticate(): ResponseEntity<Void> {
        val uri = chzzkAuthService.generateAuthUri()

        return ResponseEntity.status(HttpStatus.FOUND)
            .header(HttpHeaders.LOCATION, uri.toString())
            .build()
    }

    @GetMapping($$"${chzzk.login-redirect-url-path}")
    fun accountInterlock(
        @RequestParam("code") code: String,
        @RequestParam("state") state: String,
    ): ResponseEntity<Void> {

        chzzkAuthService.authenticate(code, state)

        val headers = HttpHeaders().apply {
            location = URI.create("https://chzzk.naver.com")
        }
        return ResponseEntity(headers, HttpStatus.FOUND)
    }
}