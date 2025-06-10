package io.github.vertxchina.infrastructure.config

import io.github.vertxchina.infrastructure.prop.ConfigProperties
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.mysqlclient.MySQLBuilder
import io.vertx.mysqlclient.MySQLConnectOptions
import io.vertx.sqlclient.Pool
import io.vertx.sqlclient.PoolOptions
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.codec.Kryo5Codec
import org.redisson.config.Config
import org.redisson.config.TransportMode
import java.util.concurrent.TimeUnit

class DBHelper(
  var config: JsonObject,
  var vertx: Vertx,
) {

  class MysqlDbHelper(
    var properties: ConfigProperties.MysqlProperties,
    var vertx: Vertx,
  ) {

    companion object {

      private lateinit var pool: Pool

      fun getPool(): Pool {
        return pool
      }
    }

    fun stop() {
      pool.close()
    }

    fun afterPropertiesSet() {
      val mysqlOptions = MySQLConnectOptions()
        .setPort(properties.port)
        .setHost(properties.host)
        .setDatabase(properties.database)
        .setUser(properties.username)
        .setPassword(properties.password)
        .setPipeliningLimit(properties.pipeliningLimit)
        .setReconnectAttempts(properties.reconnectAttempts)
        .setCharset(properties.charset)
        .setCollation(properties.collation)
        .setReconnectInterval(properties.reconnectInterval)

      val poolOptions = PoolOptions()
        .setMaxSize(properties.maxPoolSize)
        .setName(properties.poolName)
        .setIdleTimeout(30)
        .setIdleTimeoutUnit(TimeUnit.SECONDS)
        .setShared(true)

      pool = MySQLBuilder
        .pool()
        .connectingTo(mysqlOptions)
        .with(poolOptions)
        .using(vertx)
        .build()
    }
  }


  class RedisDbHelper(
    var properties: ConfigProperties.RedissonProperties,
    var vertx: Vertx,
  ) {

    companion object {
      private lateinit var client: RedissonClient

      fun getClient(): RedissonClient {
        return client
      }
    }

    fun stop() {
      client.shutdown()
    }

    fun afterPropertiesSet() {

      val config = Config()
        .setThreads(16)
        .setNettyThreads(32)
        .setCodec(Kryo5Codec())
        .setTransportMode(TransportMode.NIO)

      config.useSingleServer().apply {
        address = properties.address
        password = properties.password
        idleConnectionTimeout = properties.idleConnectionTimeout
        connectTimeout = properties.connectTimeout
        timeout = properties.timeout
        retryAttempts = properties.retryAttempts
        retryInterval = properties.retryInterval
        subscriptionsPerConnection = properties.subscriptionsPerConnection
        subscriptionConnectionMinimumIdleSize = properties.subscriptionConnectionMinimumIdleSize
        subscriptionConnectionPoolSize = properties.subscriptionConnectionPoolSize
        connectionMinimumIdleSize = properties.connectionMinimumIdleSize
        connectionPoolSize = properties.connectionPoolSize
        database = properties.database
        dnsMonitoringInterval = properties.dnsMonitoringInterval.toLong()
        this
      }
      client = Redisson.create(config)
    }

  }


  companion object {
    private var mysqlDbHelper: MysqlDbHelper? = null
    private var redisDbHelper: RedisDbHelper? = null

    val MysqlDbHelper = {
      mysqlDbHelper!!
    }

    val RedisDbHelper = {
      redisDbHelper!!
    }

    @JvmStatic
    fun stop() {
      MysqlDbHelper.invoke().stop()
      RedisDbHelper.invoke().stop()
    }
  }

  fun afterPropertiesSet() {
    val mysqlConfig = AppConfig.getMysqlConfig(config)
    if (mysqlConfig.enabled) {
      val mysqlDbHelper = MysqlDbHelper(mysqlConfig, vertx)
      mysqlDbHelper.afterPropertiesSet()
    }

    val redisConfig = AppConfig.getRedisConfig(config)
    if (redisConfig.enabled) {
      val redisDbHelper = RedisDbHelper(redisConfig, vertx)
      redisDbHelper.afterPropertiesSet()
    }
  }

}
