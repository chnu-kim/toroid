package me.chnu.toroid.domain.chzzk.realtime

import me.chnu.toroid.domain.chzzk.ChannelId
import java.net.URI

interface ChzzkSessionManager {
    fun connect(channelId: ChannelId, sessionUrl: URI)
    fun disconnect(channelId: ChannelId)
}