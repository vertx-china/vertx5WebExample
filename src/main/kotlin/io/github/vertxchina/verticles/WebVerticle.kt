package io.github.vertxchina.verticles

import io.github.vertxchina.infrastructure.config.AppConfig
import io.github.vertxchina.interfaces.web.MainRoute
import io.vertx.core.internal.logging.LoggerFactory
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.coroutines.CoroutineRouterSupport
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.coAwait

class WebVerticle(var appConfig: JsonObject) : CoroutineVerticle(), CoroutineRouterSupport {

  private val logger = LoggerFactory.getLogger(javaClass)

  override suspend fun start() {
    AppConfig.getHttpConfig(appConfig).let {
      val httpPort = it.getInteger("port", 8080)
      val server = vertx.createHttpServer()
      server.requestHandler(MainRoute.create(vertx))
        .listen(httpPort)
        .coAwait()
    }
  }
}
