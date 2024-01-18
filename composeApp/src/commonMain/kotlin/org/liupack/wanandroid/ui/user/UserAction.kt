package org.liupack.wanandroid.ui.user

sealed class UserAction {
    data object Login : UserAction()
    data object Logout : UserAction()
    data object Refresh : UserAction()
    data object OpenUser : UserAction()
}