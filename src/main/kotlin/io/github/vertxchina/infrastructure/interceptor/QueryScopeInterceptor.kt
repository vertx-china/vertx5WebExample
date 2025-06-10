package io.github.vertxchina.infrastructure.interceptor


import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext

class QueryScopeInterceptor : HandlerInterceptor {
  override suspend fun preHandle(context: RoutingContext): Boolean {
    val user = context.get<JsonObject>("user")
    if (user == null) {
      context.response().statusCode = 403
      context.json(R.fail<String>(R.REQUESTS_TOO_FREQUENTLY))
      return false
    }

    val roles = user.getJsonArray("roles", JsonObject().put("roles", listOf<String>()).getJsonArray("roles"))
    val permissions =
      user.getJsonArray("permissions", JsonObject().put("permissions", listOf<String>()).getJsonArray("permissions"))

    val path = context.request().path()
    val method = context.request().method().name()

    if (!hasPermission(roles.toList(), permissions.toList(), path, method)) {
      context.response().statusCode = 403
      context.json(R.fail<String>(R.REQUESTS_TOO_FREQUENTLY))
      return false
    }

    val queryParams = context.queryParams()
    val modifiedParams = queryParams.names().associateWith { name ->
      when (name) {
        "organizationId" -> user.getString("organizationId", "*")
        "userId" -> user.getString("id")
        else -> queryParams.get(name)
      }
    }

    // 将修改后的查询参数存储在上下文中
    context.put("scopedQueryParams", JsonObject(modifiedParams))
    return true
  }

  override suspend fun postHandle(context: RoutingContext) {
    // 后置处理，可以添加一些审计日志等
  }

  override suspend fun afterCompletion(context: RoutingContext, ex: Throwable?) {
    // 完成处理，可以进行一些清理工作
  }

  private fun hasPermission(roles: List<Any>, permissions: List<Any>, path: String, method: String): Boolean {
    // 超级管理员角色拥有所有权限
    if (roles.contains("ADMIN")) {
      return true
    }

    // 检查具体权限
    val requiredPermission = "${method.lowercase()}:$path"
    return permissions.any { permission ->
      val permStr = permission.toString()
      permStr == requiredPermission || permStr.endsWith(":*") && path.startsWith(permStr.substringBefore(":*"))
    }
  }
}
