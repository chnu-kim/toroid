package me.chnu.toroid.domain.chzzk

import me.chnu.toroid.config.chzzk.ChzzkClientId
import me.chnu.toroid.config.chzzk.ChzzkClientSecret

data class ChzzkResponse<T>(
    val code: Int,
    val message: String?,
    val content: T?,
)

data class TokenRequest(
    val grantType: String = "authorization_code",
    val clientId: ChzzkClientId,
    val clientSecret: ChzzkClientSecret,
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
    val channelId: String,
    val channelName: String,
)