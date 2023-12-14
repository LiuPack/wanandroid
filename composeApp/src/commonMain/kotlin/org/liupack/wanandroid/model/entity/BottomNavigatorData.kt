package org.liupack.wanandroid.model.entity

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ViewKanban
import androidx.compose.ui.graphics.vector.ImageVector
import org.liupack.wanandroid.router.Router

sealed class BottomNavigatorData(val icon: ImageVector, val label: String, val router: Router) {
    data object Home : BottomNavigatorData(Icons.Outlined.Home, "首页", Router.Home)
    data object System : BottomNavigatorData(Icons.Outlined.ViewKanban, "体系", Router.System)
    data object WechatAccount :
        BottomNavigatorData(Icons.Outlined.LibraryBooks, "公众号", Router.WechatAccount)

    data object Project : BottomNavigatorData(Icons.Outlined.Book, "项目", Router.Project)
    data object User : BottomNavigatorData(Icons.Outlined.Person, "我的", Router.User)
}