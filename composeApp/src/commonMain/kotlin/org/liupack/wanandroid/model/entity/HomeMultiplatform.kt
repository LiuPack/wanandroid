package org.liupack.wanandroid.model.entity

sealed class HomeMultiplatform {
    data class Pinned(val data: HomeArticleItemData) : HomeMultiplatform()
    data class Default(val data: HomeArticleItemData) : HomeMultiplatform()
}