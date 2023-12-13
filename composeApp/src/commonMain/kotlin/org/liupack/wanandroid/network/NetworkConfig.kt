package org.liupack.wanandroid.network

object NetworkConfig {
    const val IP = "https://www.wanandroid.com/"

    // 需要登录的状态值
    const val NoLogin = -1001

    // 数据成功状态
    const val Success = 0

    const val loginApi = "user/login"

    const val registerApi = "user/register"

    const val logout = "user/logout/json"
}