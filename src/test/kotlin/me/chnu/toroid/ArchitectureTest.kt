package me.chnu.toroid

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.architecture.KoArchitectureCreator
import com.lemonappdev.konsist.api.architecture.Layer
import io.kotest.core.spec.style.StringSpec

class ArchitectureTest : StringSpec({

    val scope = Konsist.scopeFromProduction()

    val presentation = Layer("Presentation", "me.chnu.toroid.presentation..")
    val application = Layer("Application", "me.chnu.toroid.application..")
    val domain = Layer("Domain", "me.chnu.toroid.domain..")
    val infrastructure = Layer("Infrastructure", "me.chnu.toroid.infrastructure..")
    val config = Layer("Config", "me.chnu.toroid.config..")

    "레이어 간 의존성 방향을 준수해야 한다" {
        with(KoArchitectureCreator) {
            scope.assertArchitecture {
                domain.dependsOnNothing()
                application.dependsOn(domain)
                presentation.dependsOn(application, domain)
                infrastructure.dependsOn(domain, config)
                config.include()
            }
        }
    }
})
