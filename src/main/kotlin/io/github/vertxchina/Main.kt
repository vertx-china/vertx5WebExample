package io.github.vertxchina

import io.github.vertxchina.verticles.MainVerticle
import io.vertx.core.Vertx
import io.vertx.core.internal.logging.LoggerFactory
import io.vertx.kotlin.core.vertxOptionsOf
import io.vertx.kotlin.coroutines.coAwait
import kotlin.time.measureTime

private val logger = LoggerFactory.getLogger("universe")

suspend fun main() {
  val time = measureTime {
    val defaultEventLoopPoolSize = Runtime.getRuntime().availableProcessors()
    val eventLoopPoolSize = System.getProperty("vertx.eventLoopPoolSize")
      ?.toIntOrNull()
      ?.takeIf { it > 0 } ?: defaultEventLoopPoolSize

    val vertxOptions = vertxOptionsOf(
      eventLoopPoolSize = eventLoopPoolSize,
      preferNativeTransport = true,
    )
    val vertx = Vertx.vertx(vertxOptions)
    vertx.exceptionHandler { throwable ->
      logger.error("未捕获的异常: ${throwable.message}", throwable)
      handleGlobalException(throwable)
    }
    vertx.deployVerticle(MainVerticle()).coAwait()
  }
  logger.debug("启动完成，耗时 ${time.inWholeMilliseconds} ms")
}

private fun handleGlobalException(throwable: Throwable) {
  logger.debug("尝试恢复服务...", throwable)
}

