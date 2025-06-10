package io.github.vertxchina.infrastructure.config

import io.github.vertxchina.infrastructure.prop.ConfigProperties
import io.vertx.config.ConfigRetriever
import io.vertx.config.ConfigRetrieverOptions
import io.vertx.config.ConfigStoreOptions
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.coroutines.coAwait

object AppConfig {

  private var config: JsonObject? = null

  /**
   * 加载配置文件
   * @param vertx Vertx实例
   * @return 配置对象
   */
  suspend fun load(vertx: Vertx): JsonObject {
    if (config != null) {
      return config!!
    }
    val configStore = ConfigStoreOptions()
      .setType("file")
      .setFormat("json")
      .setConfig(JsonObject().put("path", "application.conf"))

    val options = ConfigRetrieverOptions().addStore(configStore)
    val retriever = ConfigRetriever.create(vertx, options)
    val result = retriever.config.coAwait()
    config = result
    return result
  }

  /**
   * 获取MySQL配置
   * @param config 配置对象
   * @return MySQL配置对象
   */
  fun getMysqlConfig(config: JsonObject): ConfigProperties.MysqlProperties {
    val json = config.getJsonObject("database").getJsonObject("mysql")
    return json.mapTo(ConfigProperties.MysqlProperties::class.java)
  }


  /**
   * 获取Redis配置
   * @param config 配置对象
   * @return Redis配置对象
   */
  fun getRedisConfig(config: JsonObject): ConfigProperties.RedissonProperties {
    val json = config.getJsonObject("redis")
    return json.mapTo(ConfigProperties.RedissonProperties::class.java)
  }

  /**
   * 获取HTTP配置
   * @param config 配置对象
   * @return HTTP配置对象
   */
  fun getHttpConfig(config: JsonObject): JsonObject {
    return config.getJsonObject("http")
  }

}
