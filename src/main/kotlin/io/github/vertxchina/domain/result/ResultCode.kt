package io.github.vertxchina.domain.result

enum class ResultCode(override val code: String, override val message: String, val messageEn: String) : IResultCode {
  SUCCESS("200", "操作成功", "Success"),
  FAILURE("10000", "操作失败", "Failure"),
  ;
}
