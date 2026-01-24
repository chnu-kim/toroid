package me.chnu.toroid.infrastructure.user

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import me.chnu.toroid.config.JwtProperties
import me.chnu.toroid.domain.user.*
import java.time.Clock
import java.time.Instant
import java.util.*
import kotlin.time.Duration
import kotlin.time.toJavaDuration

class JwtTokenProvider(
    private val algorithm: Algorithm,
    private val jwtProperties: JwtProperties,
) : TokenGenerator, TokenValidator {
    override fun generateAccessToken(id: UUID) = JWT.create()
        .withSubject(id.toString())
        .withExpiresAt(jwtProperties.accessTokenExpiresIn.toInstant())
        .withIssuedAt(Instant.now(Clock.systemUTC()))
        .sign(algorithm)
        .let(::AccessToken)
        .let {
            AccessTokenResponse(it, jwtProperties.accessTokenExpiresIn)
        }

    override fun generateRefreshToken(): RefreshTokenResponse {
        return UUID.randomUUID()
            .toString()
            .let(::RefreshToken)
            .let {
                RefreshTokenResponse(it, jwtProperties.refreshTokenExpiresIn)
            }
    }

    override fun verifyToken(token: AccessToken): Boolean {
        try {
            JWT.require(algorithm)
                .withIssuer(jwtProperties.issuer)
                .build()
                .verify(token.value)
            return true
        } catch (e: JWTVerificationException) {
            return false
        }
    }

    override fun extractUserId(token: AccessToken): UUID {
        val decodedJwt = JWT.require(algorithm)
            .withIssuer(jwtProperties.issuer)
            .build()
            .verify(token.value)

        return UUID.fromString(decodedJwt.subject)
    }

    private fun Duration.toInstant(): Instant {
        return Instant
            .now(Clock.systemUTC())
            .plus(this.toJavaDuration())
    }
}