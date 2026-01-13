package me.chnu.chzzk.config.chzzk

import org.springframework.boot.context.properties.ConfigurationProperties
import java.net.URI

@JvmInline
value class ChzzkClientId(val value: String)

@JvmInline
value class ChzzkClientSecret(val value: String)

@ConfigurationProperties(prefix = "chzzk")
data class ChzzkProperties(
    val clientId: ChzzkClientId,
    val clientSecret: ChzzkClientSecret,
    val loginRedirectUrlPath: String,
    val baseUrl: URI,
)