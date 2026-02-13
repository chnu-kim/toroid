package me.chnu.toroid.presentation

import me.chnu.toroid.application.ChzzkAuthUseCase
import me.chnu.toroid.domain.chzzk.auth.ChzzkAuthService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class ChzzkOAuth2Controller(
    private val authService: ChzzkAuthService,
    private val chzzkAuthUseCase: ChzzkAuthUseCase,
) {

    @GetMapping("/chzzk/authentication")
    fun authenticate(): ResponseEntity<Unit> {
        val uri = authService.getAuthUri()

        return ResponseEntity.status(HttpStatus.FOUND)
            .header(HttpHeaders.LOCATION, uri.toString())
            .build()
    }

    @GetMapping($$"${chzzk.login-redirect-url-path}")
    fun accountInterlock(
        @RequestParam("code") code: String,
        @RequestParam("state") state: String,
    ): ResponseEntity<AuthResponse> {
        val authResponse = chzzkAuthUseCase.loadUser(code, state)
        val headers = HttpHeaders().apply {
            location = URI.create("https://chzzk.naver.com")
        }
        val response = AuthResponse(
            authResponse.accessToken,
            authResponse.refreshToken,
            authResponse.accessTokenExpiresIn,
            authResponse.refreshTokenExpiresIn,
        )

        return ResponseEntity(response, headers, HttpStatus.OK)
    }
}

