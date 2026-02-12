package me.chnu.toroid.domain.chzzk.auth

import me.chnu.toroid.config.chzzk.ChzzkProperties
import me.chnu.toroid.domain.chzzk.ChzzkClient
import me.chnu.toroid.domain.chzzk.UserResponse
import me.chnu.toroid.domain.chzzk.realtime.ChzzkSessionManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.util.*
import kotlin.time.Duration.Companion.days
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Service
class ChzzkAuthService(
    @Value($$"${app.public-base-url}")
    private val baseUrl: String,
    private val chzzkProperties: ChzzkProperties,
    private val chzzkClient: ChzzkClient,
    private val stateStorage: StateStorage,
    private val chzzkTokenStorage: ChzzkTokenStorage,
    private val chzzkSessionManager: ChzzkSessionManager
) {

    companion object {
        private const val CHZZK_AUTH_BASE_URL = "https://chzzk.naver.com/account-interlock"
    }

    fun authenticate(code: String, state: String): UserResponse {
        if (!stateStorage.isStateValid(state)) {
            throw RuntimeException("Invalid state")
        }

        val tokenResponse = chzzkClient.requestAccessToken(code, state)
        val userResponse = chzzkClient.getUserInfo(tokenResponse.accessToken)

        chzzkTokenStorage.storeAccessToken(
            userResponse.channelId,
            tokenResponse.accessToken,
            tokenResponse.expiresIn.toDuration(DurationUnit.SECONDS)
        )

        chzzkTokenStorage.storeRefreshToken(
            userResponse.channelId,
            tokenResponse.refreshToken,
            30.days
        )

        return userResponse
    }

    fun generateAuthUri(): URI {
        val state = UUID.randomUUID().toString()

        val uri = UriComponentsBuilder.fromUriString(CHZZK_AUTH_BASE_URL)
            .queryParam("clientId", chzzkProperties.clientId.value)
            .queryParam("redirectUri", baseUrl + chzzkProperties.loginRedirectUrlPath)
            .queryParam("state", state)
            .build()
            .toUri()

        stateStorage.storeState(state)
        return uri
    }

}
