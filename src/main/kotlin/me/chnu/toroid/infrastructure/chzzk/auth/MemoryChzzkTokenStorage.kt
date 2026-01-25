package me.chnu.toroid.infrastructure.chzzk.auth

import me.chnu.toroid.domain.chzzk.auth.ChzzkTokenStorage
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration

@Component
class MemoryChzzkTokenStorage : ChzzkTokenStorage {
    private val repository: ConcurrentHashMap<String, String> = ConcurrentHashMap()

    override fun storeToken(key: String, value: String, ttl: Duration) {
        repository[key] = value
    }

    override fun getToken(key: String): String? {
        return repository[key]
    }

}