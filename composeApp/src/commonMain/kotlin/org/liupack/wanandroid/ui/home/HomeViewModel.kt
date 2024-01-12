package org.liupack.wanandroid.ui.home

import androidx.compose.foundation.lazy.LazyListState
import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.liupack.wanandroid.common.Constants
import org.liupack.wanandroid.model.Repository
import org.liupack.wanandroid.model.entity.HomeArticleItemData
import org.liupack.wanandroid.openUrl

class HomeViewModel(private val repository: Repository) : ViewModel() {

    var lazyListState = LazyListState()

    private val mPagingState = MutableStateFlow<PagingData<HomeArticleItemData>>(PagingData.empty())
    val pagingState = mPagingState.asStateFlow()

    fun dispatch(action: HomeAction) {
        viewModelScope.launch {
            when (action) {
                is HomeAction.Refresh -> {

                }

                is HomeAction.OpenGithub -> {
                    openUrl(Constants.projectUrl)
                }

                is HomeAction.OpenBanner -> {
                    openUrl(action.url)
                }
            }
        }
    }

    val articles = repository.articles().cachedIn(viewModelScope)

    private fun articles() {
        viewModelScope.launch {
            repository.articles().cachedIn(viewModelScope).map {
                mPagingState.value = it
            }.launchIn(viewModelScope)
        }
    }
}