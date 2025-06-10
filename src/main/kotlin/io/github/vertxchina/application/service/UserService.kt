package io.github.vertxchina.application.service

import io.github.vertxchina.domain.entits.UserInfo
import io.github.vertxchina.domain.result.R
import io.github.vertxchina.infrastructure.config.DBHelper
import io.github.vertxchina.infrastructure.config.JwtHelper
import io.vertx.core.internal.logging.LoggerFactory
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.JWTOptions
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.coroutines.coAwait
import io.vertx.sqlclient.Tuple

class UserService {

  private val logger = LoggerFactory.getLogger(javaClass)

  val pool = DBHelper.MysqlDbHelper.getPool()

  val LIST_QUERY = pool.preparedQuery("SELECT * FROM USER  ORDER BY ID DESC LIMIT 1000")
  val FIND_BYID_QUERY = pool.preparedQuery("SELECT * FROM MX_USER WHERE ID=? ")

  suspend fun list(): List<UserInfo> {
    val res = LIST_QUERY.execute().coAwait()
    return res.map { it.toJson().mapTo(UserInfo::class.java) }
  }

  fun newToken(ctx: RoutingContext) {
    val jwtAuth = JwtHelper.getAuth()
    val token = jwtAuth.generateToken(JsonObject(), JWTOptions().setExpiresInSeconds(60))
    ctx
      .json(R.data(token))
  }

  suspend fun list(ctx: RoutingContext) {
    ctx.json(R.data(list()))
  }

  suspend fun detail(ctx: RoutingContext) {
    val id = ctx.pathParam("id")
    val res = FIND_BYID_QUERY.execute(Tuple.of(id)).coAwait()
    ctx
      .json(R.data(res.single().toJson().mapTo(UserInfo::class.java)))
  }

}
