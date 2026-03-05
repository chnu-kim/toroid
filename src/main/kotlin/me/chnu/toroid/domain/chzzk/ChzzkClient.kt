package me.chnu.toroid.domain.chzzk

import org.springframework.data.domain.Pageable
import java.net.URI

interface ChzzkClient {

    fun requestAccessToken(chzzkCode: String, chzzkState: String): TokenResponse

    fun getUserInfo(accessToken: String): UserResponse

    fun getSessionUrl(accessToken: String): URI

    fun getSessions(accessToken: String, pageable: Pageable): List<Session>

    fun subscribeChat(sessionKey: String)

    fun unsubscribeChat(sessionKey: String)

    fun subscribeDonation(sessionKey: String)

    fun unsubscribeDonation(sessionKey: String)

    fun subscribeSubscription(sessionKey: String)

    fun unsubscribeSubscription(sessionKey: String)
}
