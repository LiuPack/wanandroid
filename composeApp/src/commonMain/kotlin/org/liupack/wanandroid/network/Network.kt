package org.liupack.wanandroid.network

import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.liupack.wanandroid.platform.httpClient

fun connect() = Network.httpClient

object Network {

    val httpClient = httpClient {
        expectSuccess = true
        engine {
            pipelining = true
        }
        defaultRequest {
            url(NetworkConfig.IP)
        }
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 3000
            connectTimeoutMillis = 3000
            socketTimeoutMillis = 3000
        }
        install(ContentNegotiation) {
            json(json = Json {
                encodeDefaults = true
                isLenient = true
                allowSpecialFloatingPointValues = true
                allowStructuredMapKeys = true
                prettyPrint = false
                useArrayPolymorphism = false
                ignoreUnknownKeys = true
            })
        }
    }
}