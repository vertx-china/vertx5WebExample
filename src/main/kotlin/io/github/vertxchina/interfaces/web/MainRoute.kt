package io.github.vertxchina.interfaces.web

import com.makebuk.universe.infrastructure.handler.DefaultExceptionHandler
import io.vertx.core.Vertx
import io.vertx.core.http.HttpMethod
import io.vertx.core.internal.logging.LoggerFactory
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.CorsHandler
import io.vertx.ext.web.handler.LoggerHandler
import io.vertx.ext.web.handler.ResponseTimeHandler

object MainRoute {
  private val logger = LoggerFactory.getLogger(javaClass)
  fun create(vertx: Vertx): Router {
    return Router.router(vertx).apply {
      route().handler(LoggerHandler.create())
      route().handler(ResponseTimeHandler.create())
      route().handler(
        CorsHandler.create()
          .allowedMethods(
            setOf(
              HttpMethod.GET,
              HttpMethod.POST,
              HttpMethod.PUT,
              HttpMethod.DELETE
            )
          )
          .allowedHeaders(
            setOf(
              "Authorization",
              "Content-Type",
              "Access-Control-Allow-Headers",
              "Access-Control-Allow-Origin",
              "Access-Control-Request-Method"
            )
          )
      )
      route().produces("application/json; charset=utf-8")
      route().handler(BodyHandler.create())
      route().subRouter(UserRoute().create(vertx))
      route().last()
        .handler {
          it.response()
            .setStatusCode(404)
            .putHeader("Content-Type", "text/html; charset=utf-8")
            .end("<h1>请求路径未找到!</h1>")
        }.failureHandler(DefaultExceptionHandler.of())
    }
  }
}

