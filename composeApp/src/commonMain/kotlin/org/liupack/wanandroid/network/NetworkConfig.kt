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

    const val wechatAccountSortApi = "wxarticle/chapters/json"

    const val wechatAccountArticleApi = "wxarticle/list/{id}/{page}/json"

    const val userShareArticles = "user/lg/private_articles/{page}/json"

    const val userAddShareArticle = "lg/user_article/add/json"

    const val deleteUserShareArticle = "lg/user_article/delete/{id}/json"

    const val userFavoriteArticles = "lg/collect/list/{page}/json"

    const val favoriteArticle = "lg/collect/{id}/json"

    const val unFavoriteArticle = "lg/uncollect_originId/{id}/json"

    const val unUserFavoriteArticle = "lg/uncollect/{id}/json"

    internal fun String.replaceRealIdApi(id: Int): String {
        return this.replace("{id}", id.toString())
    }

    internal fun String.replaceRealPageApi(page: Int): String {
        return this.replace("{page}", page.toString())
    }

    internal fun String.replaceRealIdAndPageApi(id: Int, page: Int): String {
        return this.replace("{id}", id.toString()).replace("{page}", page.toString())
    }
}