package me.chnu.chzzk.infrastructure.chzzk

import me.chnu.chzzk.config.chzzk.ChzzkProperties
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Component
class ChzzkClient(
    private val chzzkProperties: ChzzkProperties,
) {

    private val client: RestClient = RestClient.builder()
        .baseUrl(chzzkProperties.baseUrl)
        .defaultHeaders { headers ->
            headers.contentType = MediaType.APPLICATION_JSON
            headers.accept = listOf(MediaType.APPLICATION_JSON)
        }
        .build()

    fun requestAccessToken(chzzkCode: String, chzzkState: String): ChzzkResponse<TokenResponse> {
        return client.post()
            .uri("/auth/v1/token")
            .body(
                TokenRequest(
                    clientId = chzzkProperties.clientId,
                    clientSecret = chzzkProperties.clientSecret,
                    code = chzzkCode,
                    state = chzzkState,
                )
            )
            .retrieve()
            .body<ChzzkResponse<TokenResponse>>()
            ?: throw IllegalStateException("Chzzk API response is null.")
    }
}
