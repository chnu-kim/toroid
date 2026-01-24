package me.chnu.toroid.config

import org.springframework.boot.context.properties.ConfigurationProperties
import kotlin.time.Duration

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val issuer: String,
    val privateKey: String,
    val publicKey: String,
    val accessTokenExpiresIn: Duration,
    val refreshTokenExpiresIn: Duration,
)