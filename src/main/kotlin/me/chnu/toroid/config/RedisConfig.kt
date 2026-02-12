package me.chnu.toroid.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.data.redis.autoconfigure.DataRedisConnectionDetails
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.connection.*
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import tools.jackson.databind.json.JsonMapper


@Configuration
class RedisConfig {
    @Primary
    @Bean("redisConfiguration")
    @ConditionalOnProperty(name = ["spring.data.redis.sentinel.master"])
    fun redisSentinelConfiguration(
        connectionDetails: DataRedisConnectionDetails
    ): RedisSentinelConfiguration {
        val sentinel = connectionDetails.sentinel
            ?: throw IllegalArgumentException("Sentinel configuration is required for Redis Sentinel")

        return RedisSentinelConfiguration().apply {
            database = sentinel.database
            setPassword(sentinel.password)
            master(sentinel.master)
            sentinelUsername = sentinel.username
            setSentinelPassword(sentinel.password)
            sentinel.nodes
                .map({ node -> RedisNode(node.host(), node.port()) })
                .forEach({ node -> this.addSentinel(node) })
        }
    }

    @Bean("redisConfiguration")
    @ConditionalOnMissingBean(RedisConfiguration::class)
    fun redisStandaloneConfiguration(
        connectionDetails: DataRedisConnectionDetails
    ): RedisStandaloneConfiguration {
        val standalone = connectionDetails.standalone
            ?: throw IllegalArgumentException("Standalone configuration is required for Redis Standalone")

        return RedisStandaloneConfiguration().apply {
            username = connectionDetails.username
            setPassword(connectionDetails.password)
            database = standalone.database
            hostName = standalone.host
            port = standalone.port
        }
    }

    @Primary
    @Bean("redisConnectionFactory")
    @DependsOn("redisConfiguration")
    fun lettuceConnectionFactory(redisConfiguration: RedisConfiguration): RedisConnectionFactory {
        return LettuceConnectionFactory(redisConfiguration)
    }

    @Bean
    @DependsOn("redisConnectionFactory")
    fun redisTemplate(
        redisConnectionFactory: RedisConnectionFactory,
        jsonMapper: JsonMapper,
    ) = RedisTemplate<String, Any>().apply {
        connectionFactory = redisConnectionFactory
        keySerializer = StringRedisSerializer()
        valueSerializer = GenericJacksonJsonRedisSerializer(jsonMapper)
        afterPropertiesSet()
    }

}