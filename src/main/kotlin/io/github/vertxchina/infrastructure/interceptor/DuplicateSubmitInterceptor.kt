package io.github.vertxchina.infrastructure.interceptor


import io.github.vertxchina.domain.result.R
import io.github.vertxchina.infrastructure.config.DBHelper
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import java.util.concurrent.TimeUnit


class DuplicateSubmitInterceptor() : HandlerInterceptor {

  var redisAPI = DBHelper.RedisDbHelper.getClient()

  override suspend fun preHandle(context: RoutingContext): Boolean {
    val method = context.request().method().name()
    if (!METHODS_TO_CHECK.contains(method)) {
      return true
    }

    val user = context.get<JsonObject>("user")
    if (user == null) {
      context.response().statusCode = 403
      context.json(R.fail<String>(R.DEFAULT_NO_ACCESS))
      return false
    }

    // 生成请求唯一标识
    val requestId = generateRequestId(context, user)
    val lockKey = "lock:$requestId"
    val lock = redisAPI.getLock(lockKey)
    val res: Boolean = lock.tryLock(
      0,
      5000L,
      TimeUnit.MILLISECONDS
    )

    if (!res) {
      context.response().statusCode = 429
      context.json(R.fail<String>(R.REQUESTS_TOO_FREQUENTLY))
      return false
    }
    // 将锁的key存储在上下文中，用于后续释放
    context.put("duplicateSubmitLockKey", lockKey)
    return true
  }

  override suspend fun postHandle(context: RoutingContext) {
    // 处理完成后不需要立即释放锁
    // 让锁自动过期，这样可以在一定时间内防止重复提交
  }

  override suspend fun afterCompletion(context: RoutingContext, ex: Throwable?) {
    // 如果发生异常，立即释放锁
    if (ex != null) {
      val lockKey = context.get<String>("duplicateSubmitLockKey")
      if (lockKey != null) {
        try {
          val lock = redisAPI.getLock(lockKey)
          lock.unlock()
        } catch (e: Exception) {
          // 忽略删除锁时的异常
        }
      }
    }
  }

  private fun generateRequestId(context: RoutingContext, user: JsonObject): String {
    val path = context.request().path()
    val userId = user.getString("id")
    val body = context.body().asString()
    return "${path}:${userId}:${body.hashCode()}"
  }

  companion object {
    private val METHODS_TO_CHECK = setOf("POST", "PUT", "DELETE")
  }
}
