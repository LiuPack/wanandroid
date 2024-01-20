package org.liupack.wanandroid.ui.user_shared

sealed class UserSharedAction {
    data class ShowDeleted(val show: Boolean) : UserSharedAction()

    data class Deleted(val id: Int) : UserSharedAction()
}