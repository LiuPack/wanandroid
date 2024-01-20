package org.liupack.wanandroid.ui.home

import org.liupack.wanandroid.model.entity.HomeArticleItemData

sealed class HomeAction {

    data object Refresh : HomeAction()
    data object OpenGithub : HomeAction()
    data class OpenBanner(val url: String) : HomeAction()
    data class Favorite(val data: HomeArticleItemData) : HomeAction()
    data class CancelFavorite(val data: HomeArticleItemData) : HomeAction()
}