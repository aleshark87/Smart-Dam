// Declaration of the Gradle extension to use
plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "5.2.0"
}
repositories {
    jcenter() // Contains the whole Maven Central + other stuff
}

dependencies {
    implementation("org.scream3r:jssc:2.8.0")
    implementation("org.apache.commons:commons-math3:3.6.1")
  	implementation ("com.google.code.gson:gson:2.8.6")
  	implementation("io.vertx:vertx-core:4.0.2")
  	implementation("io.vertx:vertx-web:4.0.2")
  	implementation("io.vertx:vertx-mysql-client:4.0.3")   
  	implementation("io.vertx:vertx-web-client:4.0.2")
}

tasks.withType<Test> {
}

application {
    //mainClassName = "application.Launcher"
}
