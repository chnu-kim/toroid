package me.chnu.chzzk.infrastructure.chzzk

import me.chnu.chzzk.config.chzzk.ChzzkClientId
import me.chnu.chzzk.config.chzzk.ChzzkClientSecret

data class TokenRequest(
    val grantType: String = "authorization_code",
    val clientId: ChzzkClientId,
    val clientSecret: ChzzkClientSecret,
    val code: String,
    val state: String,
)