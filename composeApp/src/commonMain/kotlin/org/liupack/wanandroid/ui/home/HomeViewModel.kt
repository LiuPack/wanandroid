package org.liupack.wanandroid.ui.home

import androidx.compose.foundation.lazy.LazyListState
import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.liupack.wanandroid.common.Constants
import org.liupack.wanandroid.model.Repository
import org.liupack.wanandroid.model.entity.HomeArticleItemData
import org.liupack.wanandroid.openUrl

class HomeViewModel(private val repository: Repository) : ScreenModel {

    var lazyListState = LazyListState()

    private val mPagingState = MutableStateFlow<PagingData<HomeArticleItemData>>(PagingData.empty())
    val pagingState = mPagingState.asStateFlow()

    fun dispatch(action: HomeAction) {
        screenModelScope.launch {
            when (action) {
                is HomeAction.Refresh -> {
                    articles()
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

    val articles = repository.articles().cachedIn(screenModelScope)

    private fun articles() {
        screenModelScope.launch {
            repository.articles().cachedIn(screenModelScope).map {
                mPagingState.value = it
            }.launchIn(screenModelScope)
        }
    }

    override fun onDispose() {
        super.onDispose()
        screenModelScope.cancel()
    }
}