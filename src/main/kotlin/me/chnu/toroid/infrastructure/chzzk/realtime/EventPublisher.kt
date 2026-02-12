package me.chnu.toroid.infrastructure.chzzk.realtime

interface EventPublisher {
    fun <T> publish(topic: String, payload: T)
}
