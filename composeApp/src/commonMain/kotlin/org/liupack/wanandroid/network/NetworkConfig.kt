package org.liupack.wanandroid.network

object NetworkConfig {
    const val IP = "https://www.wanandroid.com/"

    // 需要登录的状态值
    const val NoLogin = -1001

    // 数据成功状态
    const val Success = 0

    const val loginApi = "user/login"

    const val registerApi = "user/register"

    const val logoutApi = "user/logout/json"

    const val userInfo = "user/lg/userinfo/json"

    const val bannerApi = "banner/json"

    const val articleApi = "article/list/{page}/json"

    const val projectSortApi = "project/tree/json"

    const val projectListApi = "project/list/{page}/json"

    const val systemBaseApi = "tree/json"

    const val userCoinCountApi = "lg/coin/userinfo/json"

    const val userCoinCountListApi = "lg/coin/list/{page}/json"

    const val coinCountRankingApi = "coin/rank/{page}/json"

    const val articleInSystemApi = "article/list/{page}/json"

    internal fun String.replaceRealPageApi(page: Int): String {
        return this.replace("{page}", page.toString())
    }
}