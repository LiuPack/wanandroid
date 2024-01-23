package org.liupack.wanandroid.ui.system.articles_in_system

sealed class ArticleInSystemAction {
    data class Favorite(val id: Int) : ArticleInSystemAction()
    data class CancelFavorite(val id: Int) : ArticleInSystemAction()
}