package io.github.vertxchina.infrastructure.utils

import io.vertx.core.ThreadingModel
import io.vertx.core.Verticle
import io.vertx.core.Vertx
import io.vertx.core.impl.cpu.CpuCoreSensor
import io.vertx.kotlin.core.deploymentOptionsOf
import io.vertx.kotlin.coroutines.coAwait
import java.util.function.Supplier

suspend fun Vertx.deployVerticleOnAllCores(verticleSupplier: Supplier<Verticle>) =
  deployVerticle(
    verticleSupplier, deploymentOptionsOf(
      instances = CpuCoreSensor.availableProcessors(),
      threadingModel = ThreadingModel.VIRTUAL_THREAD
    )
  ).coAwait()
