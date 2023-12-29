package org.liupack.wanandroid.model.entity

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.ui.graphics.vector.ImageVector

sealed class UserNavigator(val icon: ImageVector, val name: String) {
    data object UserCoinCount : UserNavigator(Icons.Outlined.AccountBalanceWallet, "我的积分")
    data object UserShared : UserNavigator(Icons.Outlined.Share, "我的分享")
    data object UserCollect : UserNavigator(Icons.Outlined.FavoriteBorder, "我的收藏")
    data object AboutUser : UserNavigator(Icons.Outlined.Person, "关于作者")
    data object SystemSetting : UserNavigator(Icons.Outlined.Settings, "系统设置")
    data object Logout : UserNavigator(Icons.Outlined.Logout, "退出登录")
}