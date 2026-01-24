package me.chnu.toroid.config

import com.auth0.jwt.algorithms.Algorithm
import me.chnu.toroid.infrastructure.user.JwtTokenProvider
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

@EnableConfigurationProperties(JwtProperties::class)
@Configuration
class JwtConfig(
    private val jwtProperties: JwtProperties,
) {

    private val keyFactory = KeyFactory.getInstance("RSA")

    @Bean
    fun jwtTokenProvider(): JwtTokenProvider {
        return JwtTokenProvider(getAlgorithm(), jwtProperties)
    }

    private fun getAlgorithm(): Algorithm {
        val publicKey = loadPublicKey(jwtProperties.publicKey)
        val privateKey = loadPrivateKey(jwtProperties.privateKey)
        return Algorithm.RSA256(publicKey, privateKey)
    }

    private fun loadPrivateKey(privateKeyPem: String): RSAPrivateKey {
        val privateKeyContent = removePemHeaders(privateKeyPem)
        val encodedBytes = Base64.getDecoder().decode(privateKeyContent)

        // PKCS#8 스펙 사용 (Java 표준)
        val keySpec = PKCS8EncodedKeySpec(encodedBytes)
        return keyFactory.generatePrivate(keySpec) as RSAPrivateKey
    }

    private fun loadPublicKey(publicKeyPem: String): RSAPublicKey {
        val publicKeyContent = removePemHeaders(publicKeyPem)
        val encodedBytes = Base64.getDecoder().decode(publicKeyContent)

        // X.509 스펙 사용 (Java 표준)
        val keySpec = X509EncodedKeySpec(encodedBytes)
        return keyFactory.generatePublic(keySpec) as RSAPublicKey
    }

    private fun removePemHeaders(pem: String): String {
        return pem
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replace("\\s".toRegex(), "") // 공백 및 줄바꿈 제거
    }

}