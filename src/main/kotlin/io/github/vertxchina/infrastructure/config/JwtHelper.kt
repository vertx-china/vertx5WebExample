package io.github.vertxchina.infrastructure.config

import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.JWTOptions
import io.vertx.ext.auth.PubSecKeyOptions
import io.vertx.ext.auth.User
import io.vertx.ext.auth.authentication.TokenCredentials
import io.vertx.ext.auth.jwt.JWTAuth
import io.vertx.ext.auth.jwt.JWTAuthOptions
import io.vertx.kotlin.coroutines.coAwait


class JwtHelper(var vertx: Vertx) {
  companion object {
    lateinit var jwtAuth: JWTAuth

    fun generateToken(json: JsonObject): String {
      return jwtAuth.generateToken(
        json,
        JWTOptions().setAlgorithm("HS256")
      )
    }

    suspend fun authenticate(token: String): User {
      return jwtAuth.authenticate(
        TokenCredentials(token)
      ).coAwait()
    }


    fun getAuth(): JWTAuth {
      return jwtAuth
    }
  }

  fun afterPropertiesSet() {
    jwtAuth = JWTAuth.create(
      vertx, JWTAuthOptions()
        .addPubSecKey(PubSecKeyOptions().apply {
          algorithm = "HS256"
          setBuffer(
            """-----BEGIN PUBLIC KEY-----
                MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0Pb63AeRH6VWemPmmU6i
                77S0XwXKrX1g6Z8IXsrhc7F5zi+i03FDXfADgdJFhQzoG0HMPA2xDDXjZYE8o+pR
                movnfyrQxMaVmoYXbZJfGqwJ8KKeyM8ZA5J7tVIUC7+Ye5KIyw8oSN4PIOhIANVU
                odS6kPvI0zK7QnF0uiz/B/kD1YoJdMmAvOv06JBr6/LsvKGVaFdCw6AzHT6vcdSx
                SsGdX/dVjp4zzyn2C4kKkJOddw2ybEF2HMztEbEULBwNeijeMDP4W/yg2+EW/epH
                R3Bm+wLilN04KZUXFmoD1YbKyz6yR8ls4yxgnXTd9Cq9ZW2u8iyzfEOpKQxKLsbK
                +wIDAQAB
            -----END PUBLIC KEY-----"""
          )
        })
        .addPubSecKey(
          PubSecKeyOptions().apply {
            algorithm = "HS256"
            setBuffer(
              """-----BEGIN PRIVATE KEY-----
                 MIIEugIBADANBgkqhkiG9w0BAQEFAASCBKQwggSgAgEAAoIBAQDQ9vrcB5EfpVZ6
                 Y+aZTqLvtLRfBcqtfWDpnwheyuFzsXnOL6LTcUNd8AOB0kWFDOgbQcw8DbEMNeNl
                 gTyj6lGai+d/KtDExpWahhdtkl8arAnwop7IzxkDknu1UhQLv5h7kojLDyhI3g8g
                 6EgA1VSh1LqQ+8jTMrtCcXS6LP8H+QPVigl0yYC86/TokGvr8uy8oZVoV0LDoDMd
                 Pq9x1LFKwZ1f91WOnjPPKfYLiQqQk513DbJsQXYczO0RsRQsHA16KN4wM/hb/KDb
                 4Rb96kdHcGb7AuKU3TgplRcWagPVhsrLPrJHyWzjLGCddN30Kr1lba7yLLN8Q6kp
                 DEouxsr7AgMBAAECgf95n0ZiygStZo3urjaJBDvRSFuy8qwETfUaBHXatjqCG8nJ
                 N/CNIF8VTZy88qWDyv9M3f8A9PcXJEY1zgJ9ZOQaD1OtsD3SScLJLUJ1g7vWykr+
                 w6enOOJWMevOnPcN33XgdmknxqqrvpMPlCJUWdXoWug7elBonB5hjaditM261ScN
                 yH2BzIHks82SoJ/esIUCQmdBOkva90zSzV2FkiKs2lgc+ZPUoygmFFpDNOKdELRk
                 D49BEAiRhue4kiSPWkOc/X+jZpSXoq+mvGhPZ+T/p5ni+Xt949vuyFIZ8Da+oOOr
                 3vxZuCCtBjbhteVemc33LH+Zj4RYwzOJYul6wIECgYEA7fmwFSZz9PCyBD5ZEsz7
                 M6aaf60uKGAYQFv40rD+JMyIA5c+qYOPvZ9JB325RjYxLBK+0zrJDbrXDy+6P1mM
                 o3b8v4FEqS9GVV2nWc/KJxGiBtPTRA4LBE4HrmX577C7g/hibq3fr1SxOZjA7CXR
                 0vlwFpT6JyWNrP9/M/KsKEECgYEA4MrH1plvSwm34nJ/K5hqKwASZ1FFXUC3yeG9
                 znOh4EIcW/1vUwj4Jc2KKhm8nSU1OaDYaBoQ04o7fvXDhZBrosTiDGwZ4vHsy9v/
                 OM47Q5iDd0bXXG/YeOMKZXPbx197J2HgShfXvOCwmktGXej71oysC930fsDBEfve
                 ySZYhDsCgYBnXkS4+yn1JDP/GeHxA669wclsWhopmYwPMKQFSLIr49fUz3Z9hVMD
                 h9Pd+CNpNxLm1QGkmO1KFHVj+FRLPieTvZSNzqW3Y3yGihauMU7a55LcqlgjQIJS
                 jV5Sx6LoFXluGo8PCmGWNBCDNzHdoZPhs02X0YBTTsGaeqSTOP7EwQKBgE02egQF
                 egvtT6NRrsrbtIQGp0ujx86rPzXx+09hupWcP799QvLjntDlU/L4GihkMm8UB6Fi
                 aqsuzRbj9f/3xnEHbZnUu+OPM8YmT/lEJWknhui+hZTzughz/AszC6fk7Rlu7iql
                 G+59w0uMVaRGNdLRSPtehu/O58J0oGa5bCgnAoGADJ4iMD6CbTv0phtqb6NBXBZG
                 jrln0PxtDnxBUWuoBd6zATFg3kNuZz/SJyfd19mv6FXIGZ/To/EsJOl/2Z2jrA3b
                 mLag539XNQj/gjXAxkt4wQU4giD8MZUDUmz/uQl95E7goJOv2Eiq09+ztcILPof0
                 qZFgi9Sc72nsoVtecWQ=
                 -----END PRIVATE KEY-----
               """
            )
          }
        ))
  }
}
