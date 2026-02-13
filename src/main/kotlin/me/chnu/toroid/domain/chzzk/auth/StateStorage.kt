package me.chnu.toroid.domain.chzzk.auth

interface StateStorage {
    /**
     * Stores the provided
     */
    fun storeState(state: String)

    /**
     * Consumes the provided state, marking it as used.
     *
     * @param state the state to be consumed
     * @return true if the state was successfully consumed, false otherwise
     */
    fun consumeState(state: String): Boolean
}
