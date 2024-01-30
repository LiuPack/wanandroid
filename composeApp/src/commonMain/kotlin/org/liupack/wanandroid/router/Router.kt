package org.liupack.wanandroid.router

import kotlinx.serialization.Serializable


@Serializable
sealed class Router(val path: String) {
    @Serializable
    data object Splash : Router("/splash")

    @Serializable
    data object Main : Router("/main")

    @Serializable
    data object Login : Router("/login")

    @Serializable
    data object Register : Router("/register")

    @Serializable
    data object Home : Router("/home")

    @Serializable
    data object System : Router("/system")

    @Serializable
    data object WechatAccount : Router("/wechat_account")

    @Serializable
    data object Project : Router("/project")

    @Serializable
    data object ProjectList : Router("/project_list")

    @Serializable
    data object User : Router("/user")

    @Serializable
    data object CoinCountRanking : Router("/coin_count_ranking")

    @Serializable
    data object ArticleInSystem : Router("/article_in_system")

    @Serializable
    data object UserCoinCount : Router("/user_coin_count")

    @Serializable
    data object WebView : Router("/web_view_content")

    @Serializable
    data object Setting : Router("/setting")

    @Serializable
    data object UserFavorite : Router("/user_favorite")

    @Serializable
    data object UserShared : Router("/user_shared")

    @Serializable
    data object UserAddShared : Router("/user_add_shared")

    @Serializable
    data object OpenSource : Router("/open_source")
}