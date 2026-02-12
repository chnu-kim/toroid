package me.chnu.toroid.domain.chzzk.auth

interface StateStorage {
    fun storeState(state: String)
    fun isStateValid(state: String): Boolean
}
