package me.chnu.toroid.infrastructure.chzzk.realtime

import org.springframework.data.redis.connection.stream.StreamRecords
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import tools.jackson.databind.json.JsonMapper

@Component
class RedisStreamEventPublisher(
    private val redisTemplate: StringRedisTemplate,
    private val jsonMapper: JsonMapper,
) : EventPublisher {
    override fun <T> publish(topic: String, payload: T) {
        val json = serializePayload(payload)

        val record = StreamRecords.newRecord()
            .ofObject(json)
            .withStreamKey(topic)

        redisTemplate.opsForStream<String, String>().add(record)
    }

    private fun <T> serializePayload(payload: T): String {
        return when (payload) {
            is String -> payload
            else -> jsonMapper.writeValueAsString(payload)
        }
    }
}
