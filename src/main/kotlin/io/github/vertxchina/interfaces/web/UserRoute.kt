package io.github.vertxchina.interfaces.web

import io.github.vertxchina.application.service.UserService
import io.github.vertxchina.infrastructure.config.JwtHelper
import io.github.vertxchina.infrastructure.interceptor.AuthorizationInterceptor
import io.github.vertxchina.infrastructure.interceptor.DuplicateSubmitInterceptor
import io.github.vertxchina.infrastructure.interceptor.QueryScopeInterceptor
import io.github.vertxchina.infrastructure.utils.coroutineHandler
import io.vertx.core.Vertx
import io.vertx.core.internal.logging.LoggerFactory
import io.vertx.ext.web.Router

class UserRoute {
  private val logger = LoggerFactory.getLogger(javaClass)

  fun create(vertx: Vertx): Router {
    val router = Router.router(vertx)
    val jwtAuth = JwtHelper.getAuth()

    // 初始化拦截器
    val authInterceptor = AuthorizationInterceptor(jwtAuth)
    val queryScopeInterceptor = QueryScopeInterceptor()
    val duplicateSubmitInterceptor = DuplicateSubmitInterceptor()

    val service = UserService()
    router.post("/user/newToken").handler(service::newToken)

    router.route("/user*").coroutineHandler { context ->
      if (authInterceptor.preHandle(context)) {
        if (queryScopeInterceptor.preHandle(context)) {
          if (duplicateSubmitInterceptor.preHandle(context)) {
            try {
              context.next()
              queryScopeInterceptor.postHandle(context)
              duplicateSubmitInterceptor.postHandle(context)
              authInterceptor.postHandle(context)
            } catch (e: Exception) {
              queryScopeInterceptor.afterCompletion(context, e)
              duplicateSubmitInterceptor.afterCompletion(context, e)
              authInterceptor.afterCompletion(context, e)
              throw e
            }
          }
        }
      }
    }
    router.get("/user/list").coroutineHandler(service::list)
    router.get("/user/:id").coroutineHandler(service::detail)
    return router
  }
}
