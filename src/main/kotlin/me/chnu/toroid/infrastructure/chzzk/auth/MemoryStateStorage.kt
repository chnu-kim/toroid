package me.chnu.toroid.infrastructure.chzzk.auth

import me.chnu.toroid.domain.chzzk.auth.StateStorage
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

@Component
class MemoryStateStorage : StateStorage {
    private val stateRepository = ConcurrentHashMap<String, Instant>()

    override fun storeState(state: String) {
        stateRepository[state] = Instant.now()
    }

    override fun consumeState(state: String): Boolean {
        val storedAt = stateRepository.remove(state) ?: return false
        return Duration.between(storedAt, Instant.now()) <= STATE_TTL
    }

    companion object {
        private val STATE_TTL = Duration.ofMinutes(10)
    }
}
