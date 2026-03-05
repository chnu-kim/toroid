package me.chnu.toroid.infrastructure.chzzk.auth

import me.chnu.toroid.domain.chzzk.ChannelId
import me.chnu.toroid.domain.chzzk.auth.ChzzkTokenStorage
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration
import kotlin.time.toJavaDuration

@Component
class MemoryChzzkTokenStorage : ChzzkTokenStorage {
    private val repository: ConcurrentHashMap<String, ExpiringToken> = ConcurrentHashMap()

    override fun storeAccessToken(
        key: ChannelId,
        value: String,
        ttl: Duration
    ) {
        repository["access_" + key.value] = ExpiringToken(value, Instant.now().plus(ttl.toJavaDuration()))
    }

    override fun storeRefreshToken(
        key: ChannelId,
        value: String,
        ttl: Duration
    ) {
        repository["refresh_" + key.value] = ExpiringToken(value, Instant.now().plus(ttl.toJavaDuration()))
    }

    override fun getAccessToken(key: ChannelId): String? {
        return getIfNotExpired("access_" + key.value)
    }

    override fun getRefreshToken(key: ChannelId): String? {
        return getIfNotExpired("refresh_" + key.value)
    }

    private fun getIfNotExpired(key: String): String? {
        val token = repository[key] ?: return null
        return if (Instant.now().isAfter(token.expiresAt)) {
            repository.remove(key)
            null
        } else {
            token.value
        }
    }

    private data class ExpiringToken(val value: String, val expiresAt: Instant)
}
