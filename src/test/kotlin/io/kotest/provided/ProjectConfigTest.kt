package io.kotest.provided

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ProjectConfigTest : StringSpec({

    "isolationMode가 InstancePerRoot로 설정되어 있다" {
        ProjectConfig.isolationMode shouldBe IsolationMode.InstancePerRoot
    }
})
