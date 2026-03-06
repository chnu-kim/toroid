@file:Suppress("InvalidPackageDeclaration")

package io.kotest.provided

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.spec.IsolationMode
import io.kotest.engine.concurrency.SpecExecutionMode

object ProjectConfig : AbstractProjectConfig() {
    override val isolationMode = IsolationMode.InstancePerRoot
    override val specExecutionMode = SpecExecutionMode.Concurrent
}
