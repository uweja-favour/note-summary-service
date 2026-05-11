import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {

    kotlin("jvm") version "2.3.0"
    kotlin("plugin.serialization") version "2.3.0"
    kotlin("plugin.spring") version "2.3.0"
    kotlin("plugin.jpa") version "2.3.0"

    id("org.springframework.boot") version "3.5.9"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "org.example"
version = "unspecified"

repositories {
    mavenCentral()
}


dependencies {


    // ---- PROJECT DEPENDENCIES ----
    implementation(project(":contracts"))
    implementation(project(":platform"))
//    implementation(project(":common:file_manager"))

    // ---- SPRING WEBFLUX STACK ----
    implementation("org.springframework.boot:spring-boot-starter-webflux") {
        // Remove Jackson Completely
        exclude(group = "com.fasterxml.jackson.core")
        exclude(group = "com.fasterxml.jackson.module")
        exclude(group = "com.fasterxml.jackson.datatype")
    }
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // ---- R2DBC ----
//    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("io.asyncer:r2dbc-mysql:1.4.1")
    implementation("io.r2dbc:r2dbc-pool")
    runtimeOnly("com.mysql:mysql-connector-j")

    // ---- COROUTINES ----
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.8.1")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

    // ---- SERIALIZATION ----

    // Kotlin 2.2 requires serialization 1.7.x+.
    val kotlinSerialization = "1.7.3"
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinSerialization")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinSerialization")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:$kotlinSerialization")

    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // ---- AWS ----
    implementation("software.amazon.awssdk:s3:2.25.0")
    implementation("software.amazon.awssdk:sts:2.25.0")

    // ---- UTIL ----
    implementation("org.jetbrains.kotlinx:atomicfu:0.24.0")


    // Ktor
    val ktor = "3.3.3" // formerly 2.3.12
    implementation("io.ktor:ktor-client-core:$ktor")
    implementation("io.ktor:ktor-client-cio:$ktor")
    implementation("io.ktor:ktor-network:${ktor}")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor")
    implementation("io.ktor:ktor-client-logging:$ktor")

    // OkHttp
    val okHttpVersion = "5.3.2"
    implementation("com.squareup.okhttp3:okhttp:$okHttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okHttpVersion")

    // ---- TOKEN-COUNTER
    implementation("com.knuddels:jtokkit:1.0.0")


    // ---- TESTING ----
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage")
    }
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("io.r2dbc:r2dbc-h2:1.0.0.RELEASE")
    testImplementation("com.h2database:h2")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.1")

    // Apache Kafka
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.apache.kafka:kafka-clients")

    // Mongo DB
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")

    /* =========================================================
     * FILE / DOCUMENT HANDLING
     * ========================================================= */
    implementation("org.apache.pdfbox:pdfbox:2.0.30")
    implementation("commons-io:commons-io:2.17.0")

    implementation("org.apache.poi:poi:5.4.0")
    implementation("org.apache.poi:poi-ooxml:5.4.0")
    implementation("org.apache.poi:poi-scratchpad:5.4.0")

    implementation("net.java.dev.jna:jna:5.13.0")
    implementation("net.sourceforge.tess4j:tess4j:5.9.0")


    implementation("org.apache.xmlbeans:xmlbeans:5.2.0")
    implementation("org.apache.commons:commons-compress:1.26.0")

}


kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.add("-Xannotation-default-target=param-property")
    }
}

tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
    jvmArgs("--enable-preview")
}
val compileKotlin: KotlinCompile by tasks


kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}