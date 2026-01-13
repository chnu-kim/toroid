package me.chnu.chzzk.infrastructure.chzzk

interface ChzzkTokenStorage {
    fun saveToken(token: ChzzkAccessToken, expiresIn: Long)
    fun getToken(): ChzzkAccessToken?
}

@JvmInline
value class ChzzkAccessToken(val value: String)