package me.chnu.toroid.infrastructure.chzzk.auth

import me.chnu.toroid.domain.chzzk.auth.ChzzkTokenStorage
import org.springframework.stereotype.Component
import kotlin.time.Duration

@Component
class MemoryChzzkTokenStorage : ChzzkTokenStorage {

    override fun storeToken(key: String, value: String, ttl: Duration) {
        TODO("Not yet implemented")
    }

    override fun getToken(key: String): String? {
        TODO("Not yet implemented")
    }


}