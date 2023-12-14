package org.liupack.wanandroid.ui.home

import androidx.compose.foundation.lazy.LazyListState
import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.liupack.wanandroid.common.Constants
import org.liupack.wanandroid.model.Repository
import org.liupack.wanandroid.model.UiState
import org.liupack.wanandroid.model.entity.BannerData
import org.liupack.wanandroid.model.entity.HomeArticleItemData
import org.liupack.wanandroid.openUrl

class HomeViewModel(private val repository: Repository) : ViewModel() {

    val lazyListState = LazyListState()

    private val mBannerList = MutableStateFlow<UiState<List<BannerData>>>(UiState.Loading)
    val bannerList = mBannerList.asStateFlow()

    private val mPagingState = MutableStateFlow<PagingData<HomeArticleItemData>>(PagingData.empty())
    val pagingState = mPagingState.asStateFlow()

    fun dispatch(action: HomeAction) {
        viewModelScope.launch {
            when (action) {
                is HomeAction.Refresh -> {
                    bannerList()
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

    private fun bannerList() {
        viewModelScope.launch {
            repository.bannerList().catch {
                mBannerList.emit(UiState.Exception(it))
            }.collectLatest {
                mBannerList.emit(UiState.Success(it))
            }
        }
    }

    private fun articles() {
        viewModelScope.launch {
            repository.articles().cachedIn(viewModelScope).collectLatest { mPagingState.emit(it) }
        }
    }
}