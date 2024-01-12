package org.liupack.wanandroid.ui.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ViewKanban
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector
import moe.tlaster.precompose.viewmodel.ViewModel
import org.liupack.wanandroid.model.Repository
import org.liupack.wanandroid.router.Router

class MainViewModel(private val repository: Repository) : ViewModel() {

    val navigationList = listOf(
        Navigation.Home,
        Navigation.System,
        Navigation.WechatAccount,
        Navigation.Project,
        Navigation.User
    )

    @Stable
    sealed class Navigation(val icon: ImageVector, val label: String, val router: Router) {
        data object Home : Navigation(
            icon = Icons.Outlined.Home,
            label = "首页",
            router = Router.Home
        )

        data object System : Navigation(
            icon = Icons.Outlined.ViewKanban,
            label = "体系",
            router = Router.System
        )

        data object WechatAccount : Navigation(
            icon = Icons.Outlined.LibraryBooks,
            label = "公众号",
            router = Router.WechatAccount
        )

        data object Project : Navigation(
            icon = Icons.Outlined.Book,
            label = "项目",
            router = Router.Project
        )

        data object User : Navigation(
            icon = Icons.Outlined.Person,
            label = "我的",
            router = Router.User
        )
    }
}
