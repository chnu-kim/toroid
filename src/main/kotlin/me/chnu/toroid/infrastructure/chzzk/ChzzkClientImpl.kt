package me.chnu.toroid.infrastructure.chzzk

import me.chnu.toroid.config.chzzk.ChzzkProperties
import me.chnu.toroid.domain.chzzk.*
import org.apache.hc.client5.http.config.ConnectionConfig
import org.apache.hc.client5.http.config.RequestConfig
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager
import org.apache.hc.core5.util.TimeValue
import org.apache.hc.core5.util.Timeout
import org.springframework.http.MediaType
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import java.net.URI


@Component
class ChzzkClientImpl(
    private val chzzkProperties: ChzzkProperties,
) : ChzzkClient {

    private val client: RestClient = RestClient.builder()
        .baseUrl(chzzkProperties.baseUrl)
        .defaultHeaders { headers ->
            headers.contentType = MediaType.APPLICATION_JSON
            headers.accept = listOf(MediaType.APPLICATION_JSON)
        }
        .requestFactory(getClientHttpRequestFactory())
        .build()

    override fun requestAccessToken(chzzkCode: String, chzzkState: String) = client.post()
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
        .validateChzzkResponse()

    override fun getUserInfo(accessToken: String) = client.get()
        .uri("/open/v1/users/me")
        .headers { headers -> headers.setBearerAuth(accessToken) }
        .retrieve()
        .body<ChzzkResponse<UserResponse>>()
        .validateChzzkResponse()

    override fun getSessionUrl(accessToken: String): URI {
        return client.get()
            .uri("/open/v1/sessions/auth")
            .headers { headers -> headers.setBearerAuth(accessToken) }
            .retrieve()
            .body<ChzzkResponse<Map<String, String>>>()
            .validateChzzkResponse()
            .let { URI.create(it["url"]!!) }
    }

    private fun <T> ChzzkResponse<T>?.validateChzzkResponse(): T {
        val response = this
            ?: throw ChzzkApiException("Chzzk API response content is null", 500)

        response.message?.let {
            throw ChzzkApiException(it, response.code)
        }

        return response.content
            ?: throw ChzzkApiException("Chzzk API response content is null with status ${response.code}", 500)
    }

    private fun getClientHttpRequestFactory(): HttpComponentsClientHttpRequestFactory {
        val connectionManager = PoolingHttpClientConnectionManager().apply {
            maxTotal = 100
            defaultMaxPerRoute = 20

            setDefaultConnectionConfig(
                ConnectionConfig.custom()
                    .setConnectTimeout(Timeout.ofSeconds(3))
                    .setSocketTimeout(Timeout.ofSeconds(10))
                    .setTimeToLive(TimeValue.ofMinutes(10))
                    .build()
            )
        }

        val requestConfig = RequestConfig.custom()
            .setConnectionRequestTimeout(Timeout.ofSeconds(3))
            .setResponseTimeout(Timeout.ofSeconds(10))
            .build()

        val httpClient = HttpClients.custom()
            .setConnectionManager(connectionManager)
            .setDefaultRequestConfig(requestConfig)
            .evictIdleConnections(TimeValue.ofSeconds(30))
            .disableAutomaticRetries()
            .disableCookieManagement()
            .build()

        return HttpComponentsClientHttpRequestFactory(httpClient)
    }

    private fun <T, U> ChzzkResponse<T>.map(transform: (T?) -> U?): ChzzkResponse<U> {
        return ChzzkResponse(
            this.code,
            this.message,
            transform(this.content),
        )
    }
}

