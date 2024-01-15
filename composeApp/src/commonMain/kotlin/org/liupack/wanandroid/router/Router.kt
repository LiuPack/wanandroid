package org.liupack.wanandroid.router

import androidx.compose.runtime.Stable

@Stable
sealed class Router(val path: String) {
    @Stable
    data object Splash : Router("/splash")

    @Stable
    data object Main : Router("/main")

    @Stable
    data object Login : Router("/login")

    @Stable
    data object Register : Router("/register")

    @Stable
    data object Home : Router("/home")

    @Stable
    data object System : Router("/system")

    @Stable
    data object WechatAccount : Router("/wechat_account")

    @Stable
    data object Project : Router("/project")

    @Stable
    data object User : Router("/user")

    @Stable
    data object CoinCountRanking : Router("/coin_count_ranking")

    @Stable
    data object ArticleInSystem : Router("/article_in_system")

    @Stable
    data object UserCoinCount : Router("/user_coin_count")

    @Stable
    data object WebView : Router("/web_view_content")
}