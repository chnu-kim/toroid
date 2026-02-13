package me.chnu.toroid.infrastructure.chzzk.auth

import me.chnu.toroid.config.chzzk.ChzzkProperties
import me.chnu.toroid.domain.chzzk.auth.AuthRequest
import me.chnu.toroid.domain.chzzk.auth.ChzzkOAuthInitiator
import me.chnu.toroid.domain.chzzk.auth.StateStorage
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.util.*

@Component
class ChzzkOAuthInitiatorImpl(
    @Value($$"${app.public-base-url}")
    private val baseUrl: String,
    private val chzzkProperties: ChzzkProperties,
    private val stateStorage: StateStorage,
) : ChzzkOAuthInitiator {

    companion object {
        private const val CHZZK_AUTH_BASE_URL = "https://chzzk.naver.com/account-interlock"
    }

    override fun issueAuthUri(): AuthRequest {
        val state = UUID.randomUUID().toString()

        val uri = UriComponentsBuilder.fromUriString(CHZZK_AUTH_BASE_URL)
            .queryParam("clientId", chzzkProperties.clientId)
            .queryParam("redirectUri", baseUrl + chzzkProperties.loginRedirectUrlPath)
            .queryParam("state", state)
            .build()
            .toUri()

        stateStorage.storeState(state)

        return AuthRequest(uri, state)
    }
}
