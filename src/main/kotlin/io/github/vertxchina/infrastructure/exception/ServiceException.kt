package io.github.vertxchina.infrastructure.exception

import io.github.vertxchina.domain.result.ResultCode


class ServiceException private constructor(
  var code: String?,
  message: String?,
  cause: Throwable?,
) : AbsException(message, cause) {
  companion object {
    private const val DEFAULT_FAIL_CODE = "-1"
    private const val DEFAULT_MESSAGE = "failure"
    fun of(code: String?): ServiceException {
      return ServiceException(code, DEFAULT_MESSAGE, null)
    }

    fun of(cause: Throwable?): ServiceException {
      return ServiceException(DEFAULT_FAIL_CODE, DEFAULT_MESSAGE, cause)
    }

    fun of(message: String?, cause: Throwable?): ServiceException {
      return ServiceException(DEFAULT_FAIL_CODE, message, cause)
    }

    fun of(code: String?, message: String?): ServiceException {
      return ServiceException(code, message, null)
    }

    fun of(code: String?, message: String?, cause: Throwable?): ServiceException {
      return ServiceException(code, message, cause)
    }

    fun of(errorCode: ResultCode): ServiceException {
      return ServiceException(errorCode.code, errorCode.message, null)
    }
  }
}

