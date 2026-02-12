package me.chnu.toroid.infrastructure.chzzk.auth

import me.chnu.toroid.domain.chzzk.ChannelId
import me.chnu.toroid.domain.chzzk.auth.ChzzkTokenStorage
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration

@Component
class MemoryChzzkTokenStorage : ChzzkTokenStorage {
    private val repository: ConcurrentHashMap<String, String> = ConcurrentHashMap()

    override fun storeAccessToken(
        key: ChannelId,
        value: String,
        ttl: Duration
    ) {
        repository["access_" + key.value] = value
    }

    override fun storeRefreshToken(
        key: ChannelId,
        value: String,
        ttl: Duration
    ) {
        repository["refresh_" + key.value] = value
    }

    override fun getAccessToken(key: ChannelId): String? {
        return repository["access_" + key.value]
    }

    override fun getRefreshToken(key: ChannelId): String? {
        return repository["refresh_" + key.value]
    }

}