package org.liupack.wanandroid.network

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T : Any> HttpResponse.dataResultBody(): DataResult<T> =
    call.body<DataResult<T>>()