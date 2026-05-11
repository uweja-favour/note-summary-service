//package com.xapps.note_summary.infrastructure.gpt_service
//
//import com.xapps.platform.serialization_core.BaseJsonEngine
//import io.ktor.client.*
//import io.ktor.client.engine.cio.*
//import io.ktor.client.plugins.*
//import io.ktor.client.plugins.contentnegotiation.*
//import io.ktor.client.plugins.logging.*
//import io.ktor.client.request.*
//import io.ktor.http.HttpHeaders.ContentEncoding
//import io.ktor.serialization.kotlinx.json.*
//import io.netty.handler.codec.compression.StandardCompressionOptions.deflate
//import io.netty.handler.codec.compression.StandardCompressionOptions.gzip
//import org.slf4j.LoggerFactory
//import java.util.concurrent.atomic.AtomicBoolean
//
//object HttpClientFactory {
//
//    private val logger = LoggerFactory.getLogger(javaClass)
//    private val isShutdown = AtomicBoolean(false)
//
//    @Volatile
//    private var httpClient: HttpClient? = null
//
//    fun getClient(): HttpClient {
//        if (isShutdown.get()) {
//            throw IllegalStateException("HttpClient has been shut down")
//        }
//
//        return httpClient ?: synchronized(this) {
//            httpClient ?: createClient().also {
//                httpClient = it
//                logger.info("🌐 HTTP Client initialized with optimized configuration")
//            }
//        }
//    }
//
//    private fun createClient(): HttpClient {
//        return HttpClient(CIO) {
//
//            // JSON parsing
//            install(ContentNegotiation) {
//                json(BaseJsonEngine.json)
//            }
//
//            // Enable gzip/deflate decompression
//            install(ContentEncoding) {
//                gzip()
//                deflate()
//            }
//
//            // Logging
//            install(Logging) {
//                logger = Logger.SIMPLE
//                level = LogLevel.INFO
//                sanitizeHeader { header ->
//                    header.lowercase() in listOf("authorization", "x-api-key", "cookie")
//                }
//            }
//
//            // Disable automatic retries (you handle them manually)
//            install(HttpRequestRetry) {
//                retryOnServerErrors(maxRetries = 0)
//                retryOnException(maxRetries = 0, retryOnTimeout = false)
//            }
//
//            // Timeouts
//            install(HttpTimeout) {
//                requestTimeoutMillis = null
//                connectTimeoutMillis = 15_000
//                socketTimeoutMillis = null
//            }
//
//            // CIO engine tuning
//            engine {
//                maxConnectionsCount = 50
//
//                endpoint {
//                    maxConnectionsPerRoute = 10
//                    pipelineMaxSize = 10
//                    keepAliveTime = 30_000
//                    connectTimeout = 15_000
//                    connectAttempts = 2
//                }
//
//                requestTimeout = 1_800_000
//
//                https {
//                    trustManager = null
//                }
//
//                proxy = null
//            }
//
//            // Default headers — cleaned up
//            defaultRequest {
//                header("User-Agent", "StudentApplication/1.0")
//                // DO NOT force Connection: close
//                // DO NOT force Accept-Encoding
//            }
//
//            // Response validation logging
//            HttpResponseValidator {
//                handleResponseExceptionWithRequest { exception, request ->
//                    logger.warn("Request to ${request.url} failed: ${exception.message}")
//                }
//            }
//        }
//    }
//
//    fun recreateClient() {
//        synchronized(this) {
//            logger.info("🔄 Recreating HTTP Client...")
//            httpClient?.close()
//            httpClient = null
//            // Next call to getClient() will create a new instance
//        }
//    }
//
//    fun shutdown() {
//        if (isShutdown.compareAndSet(false, true)) {
//            synchronized(this) {
//                httpClient?.let { client ->
//                    try {
//                        client.close()
//                        logger.info("✅ HTTP Client shut down successfully")
//                    } catch (e: Exception) {
//                        logger.warn("⚠️ Error shutting down HTTP Client: ${e.message}")
//                    } finally {
//                        httpClient = null
//                    }
//                }
//            }
//        }
//    }
//
//    // Health check method
//    fun isHealthy(): Boolean {
//        return try {
//            !isShutdown.get() && httpClient != null
//        } catch (e: Exception) {
//            false
//        }
//    }
//
//    // Method to get client stats for monitoring
//    fun getClientInfo(): String {
//        return buildString {
//            append("HttpClient Status: ")
//            append(if (isShutdown.get()) "SHUTDOWN" else "ACTIVE")
//            append(", Instance: ")
//            append(if (httpClient != null) "AVAILABLE" else "NULL")
//        }
//    }
//}