package me.chnu.toroid.domain.chzzk

@JvmInline
value class ChannelId(val value: String)

data class ChzzkResponse<T>(
    val code: Int,
    val message: String?,
    val content: T?,
)

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
