package io.github.vertxchina.verticles

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.github.vertxchina.infrastructure.config.AppConfig
import io.github.vertxchina.infrastructure.config.DBHelper
import io.github.vertxchina.infrastructure.config.JwtHelper
import io.github.vertxchina.infrastructure.utils.deployVerticleOnAllCores
import io.vertx.core.eventbus.MessageConsumer
import io.vertx.core.internal.logging.LoggerFactory
import io.vertx.core.json.JsonObject
import io.vertx.core.json.jackson.DatabindCodec
import io.vertx.kotlin.coroutines.CoroutineEventBusSupport
import io.vertx.kotlin.coroutines.CoroutineVerticle

class MainVerticle : CoroutineVerticle(), CoroutineEventBusSupport {

  private val logger = LoggerFactory.getLogger(javaClass)

  override suspend fun start() {
    try {
      val config = AppConfig.load(vertx)
      init(config).apply {
        vertx.deployVerticleOnAllCores({
          WebVerticle(config)
        })
      }
    } catch (e: Exception) {
      logger.error("加载配置文件失败: ${e.message}", e)
    }
  }

  suspend fun init(config: JsonObject) {
    val mapper = DatabindCodec.mapper()
    mapper.registerModule(JavaTimeModule())
    DBHelper(config, vertx).afterPropertiesSet()

    val jwtHelper = JwtHelper(vertx)
    jwtHelper.afterPropertiesSet()
  }

  override suspend fun stop() {
    DBHelper.Companion.stop()
  }

  private fun <T> awaitEventBusMessage(address: String): MessageConsumer<T> = vertx.eventBus().consumer<T>(address)
}
