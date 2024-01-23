package org.liupack.wanandroid.ui.wechat_account.child

sealed class ArticleInWechatAccountAction {

    data object Refresh : ArticleInWechatAccountAction()
    data class Favorite(val id: Int) : ArticleInWechatAccountAction()
    data class CancelFavorite(val id: Int) : ArticleInWechatAccountAction()
}