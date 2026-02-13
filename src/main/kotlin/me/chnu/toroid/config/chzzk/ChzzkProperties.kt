package me.chnu.toroid.config.chzzk

import org.springframework.boot.context.properties.ConfigurationProperties
import java.net.URI

@ConfigurationProperties(prefix = "chzzk")
data class ChzzkProperties(
    val clientId: String,
    val clientSecret: String,
    val loginRedirectUrlPath: String,
    val baseUrl: URI,
)
