plugins {
  kotlin ("jvm") version "2.0.0"
  application
  id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "io.github.vertxchina"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val vertxVersion = "5.0.0"
val junitJupiterVersion = "5.9.1"
val redissonVersion = "3.45.0"
val mainVerticleName = "io.github.vertxchina.vertx5WebExample.MainKt"

dependencies {
  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  implementation("io.vertx:vertx-web")
  implementation("io.vertx:vertx-web-client")

  implementation("io.vertx:vertx-config")
  implementation("io.vertx:vertx-mqtt")
  implementation("io.vertx:vertx-auth-jwt")
  implementation("io.vertx:vertx-jdbc-client")
  implementation("io.vertx:vertx-mysql-client")
  implementation("io.vertx:vertx-kafka-client")
  implementation("io.vertx:vertx-auth-oauth2")
  implementation("io.vertx:vertx-lang-kotlin")
  implementation("io.vertx:vertx-lang-kotlin-coroutines")
  implementation("com.fasterxml.jackson.core:jackson-databind")
  implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.1")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
  implementation("org.apache.poi:poi-ooxml:5.2.3")
  implementation("mysql:mysql-connector-java:8.0.30")
  implementation("org.redisson:redisson:${redissonVersion}")
  implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.18.0")
  testImplementation("org.apache.logging.log4j:log4j-core:2.18.0")
  implementation("com.lmax:disruptor:3.4.4")
  implementation("org.apache.commons:commons-lang3:3.12.0")

  testImplementation("io.vertx:vertx-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
}


kotlin.jvmToolchain(21)
application.mainClass.set(mainVerticleName)
