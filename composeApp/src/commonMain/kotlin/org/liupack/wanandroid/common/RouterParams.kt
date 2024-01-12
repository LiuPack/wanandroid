package org.liupack.wanandroid.common

import org.liupack.wanandroid.router.Router

fun mapToUrlParams(params: Map<String, Any>): String {
    return params.entries.joinToString("&") { "${it.key}=${it.value}" }
}

fun mapToUrlParams(vararg params: Pair<String, Any>): String {
    val map = mapOf(*params)
    return mapToUrlParams(map)
}

fun Router.parametersOf(params: Map<String, Any>): String {
    return path.plus("?").plus(mapToUrlParams(params))
}

fun Router.parametersOf(vararg params: Pair<String, Any>): String {
    return path.plus("?").plus(mapToUrlParams(*params))
}