package io.github.vertxchina.infrastructure.interceptor

import io.vertx.ext.web.RoutingContext

interface HandlerInterceptor {
  /**
   * 前置处理
   */
  suspend fun preHandle(context: RoutingContext): Boolean

  /**
   * 后置处理
   */
  suspend fun postHandle(context: RoutingContext)

  /**
   * 完成后处理
   */
  suspend fun afterCompletion(context: RoutingContext, ex: Throwable?)
}
