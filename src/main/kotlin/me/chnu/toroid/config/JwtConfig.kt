package me.chnu.toroid.config

import com.auth0.jwt.algorithms.Algorithm
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableConfigurationProperties(JwtProperties::class)
@Configuration
class JwtConfig(
    private val jwtProperties: JwtProperties,
) {
    @Bean
    fun getAlgorithm(): Algorithm {
        val publicKey = jwtProperties.publicKey
        val privateKey = jwtProperties.privateKey
        return Algorithm.RSA256(publicKey, privateKey)
    }
}
