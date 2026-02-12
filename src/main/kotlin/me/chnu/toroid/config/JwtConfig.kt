package me.chnu.toroid.config

import com.auth0.jwt.algorithms.Algorithm
import me.chnu.toroid.infrastructure.user.JwtTokenProvider
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableConfigurationProperties(JwtProperties::class)
@Configuration
class JwtConfig(
    private val jwtProperties: JwtProperties,
) {

    @Bean
    fun jwtTokenProvider(): JwtTokenProvider {
        return JwtTokenProvider(getAlgorithm(), jwtProperties)
    }

    private fun getAlgorithm(): Algorithm {
        val publicKey = jwtProperties.publicKey
        val privateKey = jwtProperties.privateKey
        return Algorithm.RSA256(publicKey, privateKey)
    }
}
