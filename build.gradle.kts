import dev.detekt.gradle.Detekt

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.detekt)
}

group = "me.chnu"
version = "0.0.1-SNAPSHOT"
description = "chzzk"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.webmvc)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.opentelemetry)
    implementation(libs.kotlin.reflect)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.httpclient5)
    implementation(libs.spring.boot.starter.data.redis)
    implementation(libs.java.jwt)
    implementation(libs.kotlin.logging)
    implementation(libs.socket.io.client)
    testImplementation(libs.spring.boot.starter.security.test)
    testImplementation(libs.spring.boot.starter.webmvc.test)
    testImplementation(libs.kotlin.test.junit5)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.konsist)
    testImplementation(libs.mockk)
    runtimeOnly(libs.postgresql)
    runtimeOnly(libs.spring.boot.docker.compose)
    testRuntimeOnly(libs.junit.platform.launcher)

    detektPlugins(libs.detekt.rules.ktlint.wrapper)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

detekt {
    config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
    parallel = true
}

tasks.withType<Detekt>().configureEach {
    autoCorrect = false
    setSource(files("src/main/kotlin", "src/test/kotlin"))
    exclude("**/build/**", "**/generated/**")
    reports {
        checkstyle.required.set(true)
        html.required.set(true)
        sarif.required.set(true)
        markdown.required.set(true)
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
