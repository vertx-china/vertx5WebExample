package io.github.vertxchina.domain.result


class PageInfo<T>() {
  /**
   * 数据集合
   */
  var data: MutableList<T>? = null

  /**
   * 当前页码
   */
  var current = 0

  /**
   * 每页显示条数
   */
  var pageSize = 0

  /**
   * 总记录数据量
   */
  var total = 0

  companion object {
    fun <T> of(current: Int, pageSize: Int, data: MutableList<T>?, total: Int): PageInfo<T> {
      val info = PageInfo<T>()
      info.current = current
      info.pageSize = pageSize
      info.data = data
      info.total = total
      return info
    }

    fun <T> of(current: Int, pageSize: Int): PageInfo<T> {
      val info = PageInfo<T>()
      info.current = current
      info.pageSize = pageSize
      return info
    }
  }
}
