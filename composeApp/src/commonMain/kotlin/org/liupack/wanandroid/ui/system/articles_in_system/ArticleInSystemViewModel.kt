package org.liupack.wanandroid.ui.system.articles_in_system

import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import org.liupack.wanandroid.model.Repository

class ArticleInSystemViewModel(
    cid: Int,
    repository: Repository
) : ScreenModel {

    val articleState = repository.articleInSystem(cid).cachedIn(screenModelScope)
}