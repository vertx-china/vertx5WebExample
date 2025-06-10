package io.github.vertxchina.infrastructure.interceptor


import io.github.vertxchina.domain.result.R
import io.vertx.ext.auth.authentication.TokenCredentials
import io.vertx.ext.auth.jwt.JWTAuth
import io.vertx.ext.web.RoutingContext

class AuthorizationInterceptor(private val jwtAuth: JWTAuth?) : HandlerInterceptor {

  override suspend fun preHandle(context: RoutingContext): Boolean {
    val token = context.request().getHeader("Authorization")?.removePrefix("Bearer ")
    if (token.isNullOrBlank()) {
      context.response().statusCode = 401
      context.json(R.fail<String>(R.DEFAULT_UNAUTHORIZED_MESSAGE))
      return false
    }
    return try {
      val tokenCredentials = TokenCredentials()
      tokenCredentials.toJson().put("token", token)
      val user = jwtAuth?.authenticate(tokenCredentials)?.await()
      context.put("user", user)
      true
    } catch (e: Exception) {
      context.response().statusCode = 401
      context.json(R.fail<String>(R.DEFAULT_UNAUTHORIZED_MESSAGE))
      false
    }
  }

  override suspend fun postHandle(context: RoutingContext) {

  }

  override suspend fun afterCompletion(context: RoutingContext, ex: Throwable?) {

  }
}
