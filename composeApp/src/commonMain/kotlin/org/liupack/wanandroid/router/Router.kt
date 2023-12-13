package org.liupack.wanandroid.router

sealed class Router(val path: String) {
    data object Login : Router("/login")
    data object Register : Router("/register")
    data object Home : Router("/home")
    data object System : Router("/system")
    data object WechatAccount : Router("/wechat_account")
    data object Project : Router("/project")
    data object User : Router("/user")
}