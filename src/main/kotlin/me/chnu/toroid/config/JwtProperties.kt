package me.chnu.toroid.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import kotlin.time.Duration
import kotlin.time.toKotlinDuration

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val issuer: String,
    val privateKey: RSAPrivateKey,
    val publicKey: RSAPublicKey,
    val accessTokenExpiresIn: Duration,
    val refreshTokenExpiresIn: Duration,
) {
    @ConstructorBinding
    constructor(
        issuer: String,
        privateKey: RSAPrivateKey,
        publicKey: RSAPublicKey,
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
