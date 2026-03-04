package me.chnu.toroid.domain.chzzk

import org.springframework.data.domain.Pageable
import java.net.URI

interface ChzzkClient {

    fun requestAccessToken(chzzkCode: String, chzzkState: String): TokenResponse

    fun getUserInfo(accessToken: String): UserResponse

    fun getSessionUrl(accessToken: String): URI

    fun getSessions(accessToken: String, pageable: Pageable): List<Session>

    fun subscribeChat(accessToken: String, sessionKey: String)

    fun unsubscribeChat(accessToken: String, sessionKey: String)

    fun subscribeDonation(accessToken: String, sessionKey: String)

    fun unsubscribeDonation(accessToken: String, sessionKey: String)

    fun subscribeSubscription(accessToken: String, sessionKey: String)

    fun unsubscribeSubscription(accessToken: String, sessionKey: String)
}
