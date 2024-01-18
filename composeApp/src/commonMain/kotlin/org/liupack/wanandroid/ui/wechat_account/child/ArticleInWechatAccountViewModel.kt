package org.liupack.wanandroid.ui.wechat_account.child

import androidx.paging.cachedIn
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.liupack.wanandroid.model.Repository

class ArticleInWechatAccountViewModel(
    private val id: Int,
    private val repository: Repository
) : ViewModel() {
    val article = repository.articleInWechatAccount(id).cachedIn(viewModelScope)
}
