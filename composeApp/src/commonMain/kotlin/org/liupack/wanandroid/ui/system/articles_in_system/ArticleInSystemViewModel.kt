package org.liupack.wanandroid.ui.system.articles_in_system

import androidx.paging.cachedIn
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.liupack.wanandroid.model.Repository

class ArticleInSystemViewModel(
    cid: Int,
    repository: Repository
) : ViewModel() {

    val articleState = repository.articleInSystem(cid).cachedIn(viewModelScope)
}