package org.liupack.wanandroid.ui.project.child

sealed class ProjectListAction {

    data class Favorite(val id: Int) : ProjectListAction()
    data class CancelFavorite(val id: Int) : ProjectListAction()
}