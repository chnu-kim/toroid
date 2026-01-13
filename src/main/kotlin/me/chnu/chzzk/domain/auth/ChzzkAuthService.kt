package me.chnu.chzzk.domain.auth

import me.chnu.chzzk.infrastructure.chzzk.ChzzkClient
import me.chnu.chzzk.infrastructure.chzzk.ChzzkTokenStorage
import me.chnu.chzzk.config.chzzk.ChzzkProperties
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.util.*

@Service
class ChzzkAuthService(
    @Value($$"${app.public-base-url}") private val baseUrl: String,
    private val chzzkProperties: ChzzkProperties,
    private val chzzkClient: ChzzkClient,
) {

    companion object {
        private const val CHZZK_AUTH_BASE_URL = "https://chzzk.naver.com/account-interlock"
    }

    fun authenticate(code: String, state: String) {
        val tokenResponse = chzzkClient.requestAccessToken(code, state)
    }

    fun generateAuthUri(): URI {
        val state = UUID.randomUUID().toString()

        return UriComponentsBuilder.fromUriString(CHZZK_AUTH_BASE_URL)
            .queryParam("clientId", chzzkProperties.clientId.value)
            .queryParam("redirectUri", baseUrl + chzzkProperties.loginRedirectUrlPath)
            .queryParam("state", state)
            .build()
            .toUri()
    }

}