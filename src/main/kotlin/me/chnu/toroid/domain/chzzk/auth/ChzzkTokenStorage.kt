package me.chnu.toroid.domain.chzzk.auth

import me.chnu.toroid.domain.chzzk.ChannelId
import kotlin.time.Duration

interface ChzzkTokenStorage {

    fun storeAccessToken(key: ChannelId, value: String, ttl: Duration)
    fun storeRefreshToken(key: ChannelId, value: String, ttl: Duration)
    fun getAccessToken(key: ChannelId): String?
    fun getRefreshToken(key: ChannelId): String?
}
