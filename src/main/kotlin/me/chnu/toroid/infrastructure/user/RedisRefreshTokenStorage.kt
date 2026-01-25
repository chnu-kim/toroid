package me.chnu.toroid.infrastructure.user

import me.chnu.toroid.config.JwtProperties
import me.chnu.toroid.domain.user.PublicId
import me.chnu.toroid.domain.user.RefreshToken
import me.chnu.toroid.domain.user.RefreshTokenStorage
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.time.Duration
import kotlin.time.toJavaDuration

@Component
class RedisRefreshTokenStorage(
    @Value($$"${jwt.secret}")
    private val secret: String,
    private val redisTemplate: StringRedisTemplate,
    private val jwtProperties: JwtProperties,
) : RefreshTokenStorage {

    override fun save(
        refreshToken: RefreshToken,
        id: PublicId,
    ) {
        val hashedToken = hash(refreshToken)
        redisTemplate.opsForValue().set(hashedToken, id.toString(), jwtProperties.refreshTokenExpiresIn.toJavaDuration())
    }

    override fun findByToken(refreshToken: RefreshToken): PublicId? {
        val hashedToken = hash(refreshToken)
        val uuid = redisTemplate.opsForValue().get(hashedToken)
        val publicId = UUID.fromString(uuid)
        return publicId?.let(::PublicId)
    }

    override fun revoke(refreshToken: RefreshToken) {
        val hashedToken = hash(refreshToken)

        redisTemplate.delete(hashedToken)
    }

    private fun hash(refreshToken: RefreshToken): String {
        try {
            val mac = Mac.getInstance("HmacSHA256")
            val secretKeySpec = SecretKeySpec(secret.toByteArray(StandardCharsets.UTF_8), "HmacSHA256")
            mac.init(secretKeySpec)

            val bytes = mac.doFinal(refreshToken.value.toByteArray(StandardCharsets.UTF_8))
            return bytesToHex(bytes)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Token hashing failed", e)
        } catch (e: InvalidKeyException) {
            throw RuntimeException("Token hashing failed", e)
        }
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (b in bytes) {
            sb.append(String.format("%02x", b))
        }
        return sb.toString()
    }
}