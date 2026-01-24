package me.chnu.toroid.domain.chzzk

interface ChzzkClient {

    fun requestAccessToken(chzzkCode: String, chzzkState: String): TokenResponse

    fun getUserInfo(accessToken: String): UserResponse
}