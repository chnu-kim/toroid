package me.chnu.toroid.presentation

import me.chnu.toroid.application.ChzzkAuthUseCase
import me.chnu.toroid.contract.http.ChzzkRoutes
import me.chnu.toroid.domain.chzzk.auth.ChzzkAuthService
import me.chnu.toroid.domain.user.RefreshToken
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import kotlin.time.toJavaDuration

@RestController
class ChzzkOAuth2Controller(
    private val authService: ChzzkAuthService,
    private val chzzkAuthUseCase: ChzzkAuthUseCase,
    @Value($$"${chzzk.redirect-url}")
    private val redirectUrl: URI,
) {

    @GetMapping("/chzzk/authentication")
    fun authenticate(): ResponseEntity<Unit> {
        val uri = authService.getAuthUri()

        return ResponseEntity.status(HttpStatus.FOUND)
            .header(HttpHeaders.LOCATION, uri.toString())
            .build()
    }

    @GetMapping(ChzzkRoutes.LOGIN_REDIRECT_PATH)
    fun accountInterlock(
        @RequestParam("code") code: String,
        @RequestParam("state") state: String,
    ): ResponseEntity<Unit> {
        val authResponse = chzzkAuthUseCase.loadUser(code, state)
        val isSecure = redirectUrl.scheme.equals("https", ignoreCase = true)
        val refreshTokenCookie = ResponseCookie.from("refresh_token", authResponse.refreshToken.value)
            .httpOnly(true)
            .secure(isSecure)
            .path("/")
            .maxAge(authResponse.refreshTokenExpiresIn.toJavaDuration())
            .sameSite("Lax")
            .build()

        val headers = HttpHeaders().apply {
            location = redirectUrl
            add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
        }

        return ResponseEntity(headers, HttpStatus.FOUND)
    }

    @PostMapping("/refresh")
    fun refreshToken(
        @CookieValue(
            name = "refresh_token",
            required = true
        ) refreshToken: RefreshToken
    ): ResponseEntity<Unit> {
        return ResponseEntity.ok().build()
    }
}
