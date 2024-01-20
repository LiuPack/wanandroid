package org.liupack.wanandroid.ui.user_shared.add_shared

sealed class UserAddSharedAction {
    data class InputTitle(val title: String) : UserAddSharedAction()
    data class InputLink(val link: String) : UserAddSharedAction()
    data object Shared : UserAddSharedAction()
}