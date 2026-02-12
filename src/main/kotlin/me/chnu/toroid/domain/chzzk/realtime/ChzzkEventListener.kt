package me.chnu.toroid.domain.chzzk.realtime

interface ChzzkEventListener {
    fun onConnected(channelId: String)
    fun onMessageReceived(message: Any)
    fun onDisconnected()
    fun onError(throwable: Throwable)
}