package org.liupack.wanandroid.ui.user_favorite

sealed class UserFavoriteAction {

    data class CancelFavorite(val id: Int, val originId: Int) : UserFavoriteAction()
}