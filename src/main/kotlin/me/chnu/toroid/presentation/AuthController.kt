package me.chnu.toroid.presentation

import me.chnu.toroid.application.ChzzkOAuthUseCase
import me.chnu.toroid.application.TokenRefreshUseCase
import me.chnu.toroid.application.TokenResponse
import me.chnu.toroid.contract.http.ChzzkRoutes
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
class AuthController(
    @Value($$"${chzzk.redirect-url}")
    private val redirectUrl: URI,
    private val chzzkOAuthUseCase: ChzzkOAuthUseCase,
    private val tokenRefreshUseCase: TokenRefreshUseCase,
) {

    @GetMapping("/chzzk/authentication")
    fun authenticate(): ResponseEntity<Unit> {
        val uri = chzzkOAuthUseCase.getAuthUri()

        return ResponseEntity.status(HttpStatus.FOUND)
            .header(HttpHeaders.LOCATION, uri.toString())
            .build()
    }

    @GetMapping(ChzzkRoutes.LOGIN_REDIRECT_PATH)
    fun accountInterlock(
        @RequestParam("code") code: String,
        @RequestParam("state") state: String,
    ): ResponseEntity<Unit> {
        val tokenResponse = chzzkOAuthUseCase.loadUser(code, state)
        val refreshTokenCookie = createRefreshTokenCookie(tokenResponse)

        val headers = HttpHeaders().apply {
            location = redirectUrl
            add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
        }

        return ResponseEntity(headers, HttpStatus.FOUND)
    }

    @PostMapping("/refresh")
    fun refreshToken(
        @CookieValue(
            name = "rt",
            required = true,
        ) refreshToken: RefreshToken
    ): ResponseEntity<AuthResponse> {
        val tokenResponse = tokenRefreshUseCase.refreshToken(refreshToken)
        val refreshTokenCookie = createRefreshTokenCookie(tokenResponse)

        val headers = HttpHeaders().apply {
            add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
        }

        val authResponse = AuthResponse(
            accessToken = tokenResponse.accessToken,
            accessTokenExpiresIn = tokenResponse.accessTokenExpiresIn,
        )

        return ResponseEntity(authResponse, headers, HttpStatus.OK)
    }

    private fun createRefreshTokenCookie(tokenResponse: TokenResponse): ResponseCookie {
        val isSecure = redirectUrl.scheme.equals("https", ignoreCase = true)
        val refreshTokenCookie = ResponseCookie.from("rt", tokenResponse.refreshToken.value)
            .httpOnly(true)
            .secure(isSecure)
            .path("/")
            .maxAge(tokenResponse.refreshTokenExpiresIn.toJavaDuration())
            .sameSite("Lax")
            .build()
        return refreshTokenCookie
    }
}
