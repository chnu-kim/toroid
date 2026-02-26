package me.chnu.toroid.presentation

import me.chnu.toroid.domain.user.AccessToken
import kotlin.time.Duration

data class AuthResponse(
    val accessToken: AccessToken,
    val accessTokenExpiresIn: Duration,
)
