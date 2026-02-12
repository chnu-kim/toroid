package me.chnu.toroid.infrastructure.chzzk.realtime

import io.github.oshai.kotlinlogging.KotlinLogging
import io.socket.client.IO
import io.socket.client.Socket
import jakarta.annotation.PreDestroy
import me.chnu.toroid.domain.chzzk.ChannelId
import me.chnu.toroid.domain.chzzk.realtime.ChzzkSessionManager
import org.springframework.stereotype.Component
import java.net.URI
import java.util.concurrent.ConcurrentHashMap

@Component
class SocketChzzkSessionManager(
    private val eventPublisher: EventPublisher,
) : ChzzkSessionManager {
    private val logger = KotlinLogging.logger { }

    private val sessions = ConcurrentHashMap<String, Socket>()

    override fun connect(channelId: ChannelId, sessionUrl: URI) {
        if (sessions.containsKey(channelId.value)) {
            val socket = checkNotNull(sessions[channelId.value])
            if (socket.connected()) {
                logger.warn { "Already connected to $channelId" }
                return
            }
            socket.off()
            socket.disconnect()
            sessions.remove(channelId.value)
        }

        val options = IO.Options().apply {
            reconnection = true
            timeout = SOCKET_TIMEOUT_MS
            forceNew = true
            transports = arrayOf("websocket")
        }
        val socket = IO.socket(sessionUrl, options)

        socket.on(Socket.EVENT_CONNECT) { args ->
            logger.info { "Connected to $channelId" }
        }
        socket.on(Socket.EVENT_DISCONNECT) { args ->
            logger.warn { "Disconnected from ${channelId.value} (reason=${args.firstOrNull()})" }
        }
        socket.on(Socket.EVENT_CONNECT_ERROR) {
            logger.warn { "Failed to connect to $channelId" }
        }
        socket.on(Chzzk.EVENT_CHAT) { args ->
            eventPublisher.publish(Chzzk.EVENT_CHAT, args.firstOrNull())
        }
        socket.on(Chzzk.EVENT_DONATION) { args ->
            eventPublisher.publish(Chzzk.EVENT_DONATION, args.firstOrNull())
        }
        socket.on(Chzzk.EVENT_SUBSCRIPTION) { args ->
            eventPublisher.publish(Chzzk.EVENT_SUBSCRIPTION, args.firstOrNull())
        }

        socket.connect()
        sessions[channelId.value] = socket
    }

    override fun disconnect(channelId: ChannelId) {
        sessions[channelId.value]?.disconnect()
        sessions[channelId.value]?.off()
        sessions.remove(channelId.value)
    }

    @PreDestroy
    fun cleanup() {
        sessions.forEach { session ->
            session.value.disconnect()
            session.value.off()
        }
        sessions.clear()
    }

    companion object {
        private const val SOCKET_TIMEOUT_MS = 3000L
    }

    private object Chzzk {
        const val EVENT_CHAT = "CHAT"
        const val EVENT_DONATION = "DONATION"
        const val EVENT_SUBSCRIPTION = "SUBSCRIPTION"
    }
}


