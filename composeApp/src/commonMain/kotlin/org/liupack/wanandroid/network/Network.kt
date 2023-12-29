package org.liupack.wanandroid.network

import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.cookie
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.decodeBase64String
import kotlinx.serialization.json.Json
import org.liupack.wanandroid.common.Constants
import org.liupack.wanandroid.platform.httpClient
import org.liupack.wanandroid.platform.settings

fun connect() = Network.httpClient

object Network {

    val httpClient = httpClient {
        expectSuccess = true
        engine {
            pipelining = true
        }
        defaultRequest {
            url(NetworkConfig.IP)
            val userName =
                settings.getStringOrNull(Constants.loginUserName)?.decodeBase64String().orEmpty()
            val password =
                settings.getStringOrNull(Constants.loginUserPassword)?.decodeBase64String()
                    .orEmpty()
            cookie(Constants.loginUserName, userName)
            cookie(Constants.loginUserPassword, password)
        }
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
        install(HttpCookies) {
            storage = AcceptAllCookiesStorage()
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