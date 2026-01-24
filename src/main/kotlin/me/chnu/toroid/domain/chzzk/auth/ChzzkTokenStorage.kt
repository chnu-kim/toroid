package me.chnu.toroid.domain.chzzk.auth

import kotlin.time.Duration

interface ChzzkTokenStorage {

    fun storeToken(key: String, value: String, ttl: Duration)
    fun getToken(key: String): String?

}
