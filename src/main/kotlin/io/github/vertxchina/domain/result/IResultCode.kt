package io.github.vertxchina.domain.result

import java.io.Serializable

interface IResultCode : Serializable {
  val message: String?
  val code: String?

  enum class P(private val prefix: String, private val desc: String) {
    RM("101", "demo测试服务"),
    ;

    fun prefix(): String? {
      return this.prefix
    }
  }
}

