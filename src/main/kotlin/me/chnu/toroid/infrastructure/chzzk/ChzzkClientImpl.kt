package me.chnu.toroid.infrastructure.chzzk

import me.chnu.toroid.config.chzzk.ChzzkProperties
import me.chnu.toroid.domain.chzzk.ChzzkApiException
import me.chnu.toroid.domain.chzzk.ChzzkClient
import me.chnu.toroid.domain.chzzk.Session
import me.chnu.toroid.domain.chzzk.TokenRequest
import me.chnu.toroid.domain.chzzk.TokenResponse
import me.chnu.toroid.domain.chzzk.UserResponse
import org.apache.hc.client5.http.config.ConnectionConfig
import org.apache.hc.client5.http.config.RequestConfig
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager
import org.apache.hc.core5.util.TimeValue
import org.apache.hc.core5.util.Timeout
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.http.client.ClientHttpRequestFactory
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
            .let { response ->
                val url = checkNotNull(response["url"]) { "Session URL is missing from response" }
                URI.create(url)
            }
    }

    override fun getSessions(
        accessToken: String,
        pageable: Pageable,
    ): List<Session> {
        return client.get()
            .uri("/open/v1/sessions") {
                it.queryParam("page", pageable.pageNumber)
                it.queryParam("size", pageable.pageSize)
                it.build()
            }
            .headers { headers -> headers.setBearerAuth(accessToken) }
            .retrieve()
            .body<ChzzkResponse<List<Session>>>()
            .validateChzzkResponse()
    }

    override fun subscribeChat(sessionKey: String) {
        client.post()
            .uri("/open/v1/sessions/events/subscribe/chat") {
                it.queryParam("sessionKey", sessionKey)
                it.build()
            }
            .retrieve()
            .toBodilessEntity()
    }

    override fun unsubscribeChat(sessionKey: String) {
        client.post()
            .uri("/open/v1/sessions/events/unsubscribe/chat") {
                it.queryParam("sessionKey", sessionKey)
                it.build()
            }
            .retrieve()
            .toBodilessEntity()
    }

    override fun subscribeDonation(sessionKey: String) {
        client.post()
            .uri("/open/v1/sessions/events/subscribe/donation") {
                it.queryParam("sessionKey", sessionKey)
                it.build()
            }
            .retrieve()
            .toBodilessEntity()
    }

    override fun unsubscribeDonation(sessionKey: String) {
        client.post()
            .uri("/open/v1/sessions/events/unsubscribe/donation") {
                it.queryParam("sessionKey", sessionKey)
                it.build()
            }
            .retrieve()
            .toBodilessEntity()
    }

    override fun subscribeSubscription(sessionKey: String) {
        client.post()
            .uri("/open/v1/sessions/events/subscribe/subscription") {
                it.queryParam("sessionKey", sessionKey)
                it.build()
            }
            .retrieve()
            .toBodilessEntity()
    }

    override fun unsubscribeSubscription(sessionKey: String) {
        client.post()
            .uri("/open/v1/sessions/events/unsubscribe/subscription") {
                it.queryParam("sessionKey", sessionKey)
                it.build()
            }
            .retrieve()
            .toBodilessEntity()
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

    private fun getClientHttpRequestFactory(): ClientHttpRequestFactory {
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

}

