package io.github.vertxchina.infrastructure.prop

object ConfigProperties {

  class MysqlProperties {
    var enabled: Boolean = false
    var host: String? = null
    var port = 3306
    var database: String? = null
    var username: String? = null
    var password: String? = null
    var charset = "utf8"
    var collation = "utf8_general_ci"
    var maxPoolSize = 20
    var pipeliningLimit = 64
    var reconnectAttempts = 5
    var reconnectInterval = 1000L
    var poolName = "p-mysql"
  }

  class RedissonProperties {
    var enabled: Boolean = false
    var address: String? = null
    var password: String? = null
    var idleConnectionTimeout = 10000
    var connectTimeout = 10000
    var timeout = 3000
    var retryAttempts = 3
    var retryInterval = 1500
    var subscriptionsPerConnection = 5
    var subscriptionConnectionMinimumIdleSize = 1
    var subscriptionConnectionPoolSize = 50
    var connectionMinimumIdleSize = 24
    var connectionPoolSize = 64
    var database = 0
    var dnsMonitoringInterval = 5000
  }
}
