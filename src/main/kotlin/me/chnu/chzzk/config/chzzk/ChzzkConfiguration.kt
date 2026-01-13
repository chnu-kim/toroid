package me.chnu.chzzk.config.chzzk

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@EnableConfigurationProperties(ChzzkProperties::class)
@Configuration
class ChzzkConfiguration {
}