package me.chnu.toroid.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import kotlin.time.Duration
import kotlin.time.toKotlinDuration

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val issuer: String,
    val privateKey: String,
    val publicKey: String,
    val accessTokenExpiresIn: Duration,
    val refreshTokenExpiresIn: Duration,
) {
    @ConstructorBinding
    constructor(
        issuer: String,
        privateKey: String,
        publicKey: String,
        accessTokenExpiresIn: java.time.Duration,
        refreshTokenExpiresIn: java.time.Duration,
    ) : this(
        issuer,
        privateKey,
        publicKey,
        accessTokenExpiresIn.toKotlinDuration(),
        refreshTokenExpiresIn.toKotlinDuration(),
    )
}