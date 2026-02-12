package me.chnu.toroid.domain.chzzk

import java.net.URI

interface ChzzkClient {

    fun requestAccessToken(chzzkCode: String, chzzkState: String): TokenResponse

    fun getUserInfo(accessToken: String): UserResponse

    fun getSessionUrl(accessToken: String): URI
}