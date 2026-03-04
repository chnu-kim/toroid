package me.chnu.toroid.domain.chzzk

import java.time.OffsetDateTime

@JvmInline
value class ChannelId(val value: String)

enum class ChzzkSessionEvent {
    CHAT,
    DONATION,
    SUBSCRIPTION,
}

data class TokenRequest(
    val grantType: String = "authorization_code",
    val clientId: String,
    val clientSecret: String,
    val code: String,
    val state: String,
)

data class TokenResponse(
    val refreshToken: String,
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Long,
    val scope: String,
)

data class UserResponse(
    val channelId: ChannelId,
    val channelName: String,
)

data class Session(
    val sessionKey: String,
    val connectedDate: OffsetDateTime,
    val disconnectedDate: OffsetDateTime,
    val subscribedEvents: List<ChzzkSessionEvent>,
    val channelId: ChannelId,
)
