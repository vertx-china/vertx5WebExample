package io.github.vertxchina.domain.entits

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class UserInfo : Serializable {

  var id: Long? = null

  var phone: String? = null

  @JsonProperty("userName", access = JsonProperty.Access.WRITE_ONLY)
  var userName: String? = null
}
