package com.makebuk.universe.infrastructure.handler

import com.makebuk.universe.domain.result.R
import com.makebuk.universe.domain.result.ResultCode
import com.makebuk.universe.infrastructure.exception.ServiceException
import io.vertx.core.Handler
import io.vertx.core.internal.logging.LoggerFactory
import io.vertx.ext.web.RoutingContext


class DefaultExceptionHandler : Handler<RoutingContext> {
  private val logger = LoggerFactory.getLogger(javaClass)

  override fun handle(ctx: RoutingContext) {
    val throwable: Throwable? = ctx.failure()

    logger.error("===> Default exception fail: ", throwable)

    if (throwable is NullPointerException) {
      ctx.json(R.fail<String>("空指针异常错误."))
      return
    }

    if (throwable is ServiceException) {
      val ex = throwable
      ctx.json(R.fail<String>(ex.code, ex.message))
      return
    }

    ctx.json(R.fail<String>(ResultCode.FAILURE))
  }

  companion object {
    @JvmStatic
    fun of(): DefaultExceptionHandler = DefaultExceptionHandler()
  }
}
