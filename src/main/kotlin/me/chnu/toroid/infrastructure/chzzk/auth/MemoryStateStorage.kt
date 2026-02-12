package me.chnu.toroid.infrastructure.chzzk.auth

import me.chnu.toroid.domain.chzzk.auth.StateStorage
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

@Component
class MemoryStateStorage : StateStorage {
    private val stateRepository = ConcurrentHashMap<String, Instant>()

    override fun storeState(state: String) {
        stateRepository[state] = Instant.now()
    }

    override fun isStateValid(state: String): Boolean {
        if (stateRepository.containsKey(state)) {
            stateRepository.remove(state)
            return true
        }
        return false
    }
}
