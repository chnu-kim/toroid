package me.chnu.toroid.domain.chzzk.auth

import me.chnu.toroid.domain.chzzk.ChzzkClient
import me.chnu.toroid.domain.chzzk.UserResponse
import org.springframework.stereotype.Service
import java.net.URI
import kotlin.time.Duration.Companion.days
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Service
class ChzzkAuthService(
    private val chzzkClient: ChzzkClient,
    private val stateStorage: StateStorage,
    private val chzzkTokenStorage: ChzzkTokenStorage,
    private val chzzkOAuthInitiator: ChzzkOAuthInitiator,
) {

    fun getAuthUri(): URI {
        val authRequest = chzzkOAuthInitiator.issueAuthUri()
        return authRequest.uri
    }

    fun authenticate(code: String, state: String): UserResponse {
        if (!stateStorage.consumeState(state)) {
            error("Invalid state")
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
}
