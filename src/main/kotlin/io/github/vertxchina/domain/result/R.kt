package io.github.vertxchina.domain.result

import io.github.vertxchina.domain.result.R.Companion.data
import java.io.Serializable
import java.util.*
import java.util.function.Function


class R<T> : Serializable {
  var code: String? = null
  var success = false
  var data: T? = null
  var msg: String? = null


  constructor(resultCode: IResultCode) : this(resultCode.code, null, resultCode.message)

  constructor(resultCode: IResultCode, msg: String?) : this(resultCode.code, null, msg)

  constructor(resultCode: IResultCode, data: T?) : this(resultCode, data, resultCode.message)

  constructor(resultCode: IResultCode, data: T?, msg: String?) : this(resultCode.code, data, msg)

  constructor(code: String?, data: T?, msg: String?) {
    this.code = code
    this.data = data
    this.success = ResultCode.SUCCESS.code == code
    this.msg = msg
  }

  companion object {
    val DEFAULT_NULL_MESSAGE: String = "暂无承载数据"
    val DEFAULT_SUCCESS_MESSAGE: String = "操作成功"
    val DEFAULT_FAILURE_MESSAGE: String = "操作失败"
    val DEFAULT_NO_ACCESS: String = "无访问权限"
    val DEFAULT_UNAUTHORIZED_MESSAGE: String = "签名认证失败"
    val REQUESTS_TOO_FREQUENTLY: String = "请求过于频繁，请稍后重试"

    /**
     * 判断返回是否为成功
     *
     * @param result Result
     * @return 是否成功
     */
    fun isSuccess(result: R<*>?): Boolean {
      return Optional.ofNullable<R<*>?>(result)
        .map(Function { x: R<*>? -> ResultCode.SUCCESS.code == x!!.code }).orElse(java.lang.Boolean.FALSE)
    }

    /**
     * 判断返回是否为成功
     *
     * @param result Result
     * @return 是否成功
     */
    fun isNotSuccess(result: R<*>?): Boolean {
      return !isSuccess(result)
    }

    /**
     * 返回R
     *
     * @param data 数据
     * @param <T>  T 泛型标记
     * @return R
    </T> */
    fun <T> data(data: T?): R<T?>? {
      return data<T?>(data, DEFAULT_SUCCESS_MESSAGE)
    }

    /**
     * 返回R
     *
     * @param data 数据
     * @param msg  消息
     * @param <T>  T 泛型标记
     * @return R
    </T> */
    fun <T> data(data: T?, msg: String?): R<T?>? {
      return data(ResultCode.SUCCESS.code, data, msg)
    }

    /**
     * 返回R
     *
     * @param code 状态码
     * @param data 数据
     * @param msg  消息
     * @param <T>  T 泛型标记
     * @return R
    </T> */
    fun <T> data(code: String?, data: T?, msg: String?): R<T?> {
      return R(code, data, if (data == null) DEFAULT_NULL_MESSAGE else msg)
    }


    /**
     * 返回R
     *
     * @param msg 消息
     * @param <T> T 泛型标记
     * @return R
    </T> */
    fun <T> success(msg: String?): R<T?> {
      return R(ResultCode.SUCCESS, msg)
    }

    /**
     * 返回R
     *
     * @param resultCode 业务代码
     * @param <T>        T 泛型标记
     * @return R
    </T> */
    fun <T> success(resultCode: IResultCode?): R<T?> {
      return R(resultCode!!)
    }

    /**
     * 返回R
     *
     * @param resultCode 业务代码
     * @param msg        消息
     * @param <T>        T 泛型标记
     * @return R
    </T> */
    fun <T> success(resultCode: IResultCode, msg: String?): R<T?> {
      return R<T?>(resultCode, msg)
    }

    /**
     * 返回R
     *
     * @param msg 消息
     * @param <T> T 泛型标记
     * @return R
    </T> */
    fun <T> fail(msg: String?): R<T?> {
      return R(ResultCode.FAILURE, msg)
    }


    /**
     * 返回R
     *
     * @param code 状态码
     * @param msg  消息
     * @param <T>  T 泛型标记
     * @return R
    </T> */
    fun <T> fail(code: String?, msg: String?): R<T?> {
      return R(code, null, msg)
    }

    /**
     * 返回R
     *
     * @param resultCode 业务代码
     * @param <T>        T 泛型标记
     * @return R
    </T> */
    fun <T> fail(resultCode: IResultCode?): R<T?> {
      return R(resultCode!!)
    }

    /**
     * 返回R
     *
     * @param resultCode 业务代码
     * @param msg        消息
     * @param <T>        T 泛型标记
     * @return R
    </T> */
    fun <T> fail(resultCode: IResultCode, msg: String?): R<T?> {
      return R(resultCode, msg)
    }

    /**
     * 返回R
     *
     * @param flag 成功状态
     * @return R
     */
    fun <T> status(flag: Boolean): R<T?> {
      return if (flag) {
        success(DEFAULT_SUCCESS_MESSAGE)
      } else {
        fail(DEFAULT_FAILURE_MESSAGE)
      }
    }
  }
}
