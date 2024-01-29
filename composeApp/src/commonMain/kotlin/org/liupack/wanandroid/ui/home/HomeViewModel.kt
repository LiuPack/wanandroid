package org.liupack.wanandroid.ui.home

import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.liupack.wanandroid.common.Constants
import org.liupack.wanandroid.model.Repository
import org.liupack.wanandroid.model.UiState
import org.liupack.wanandroid.model.entity.HomeArticleItemData
import org.liupack.wanandroid.model.usecase.CancelFavoriteArticleUseCase
import org.liupack.wanandroid.model.usecase.FavoriteArticleUseCase
import org.liupack.wanandroid.openUrl

typealias CombinePinnedAndArticles = Pair<List<HomeArticleItemData>, Flow<PagingData<HomeArticleItemData>>>

class HomeViewModel(
    private val repository: Repository,
    private val favoriteArticleUseCase: FavoriteArticleUseCase,
    private val cancelFavoriteArticleUseCase: CancelFavoriteArticleUseCase
) : ViewModel() {
    init {
        combineArticle()
    }

    private val mCombineData =
        MutableStateFlow<CombinePinnedAndArticles>(Pair(emptyList(), emptyFlow()))

    val combineData = mCombineData.asStateFlow()

    private val mFavoriteState = MutableSharedFlow<UiState<Boolean>>()
    val favoriteState = mFavoriteState.asSharedFlow()

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

                is HomeAction.Favorite -> {
                    favoriteArticle(action.data.id)
                }

                is HomeAction.CancelFavorite -> {
                    cancelFavoriteArticle(action.data.id)
                }
            }
        }
    }

    private fun combineArticle() {
        viewModelScope.launch {
            val pinnedAsync = async { repository.pinnedArticles() }
            val articlesAsync = repository.articles().cachedIn(viewModelScope)
            pinnedAsync.await().map {
                Pair(it, articlesAsync)
            }.collectLatest {
                mCombineData.emit(it)
            }
        }
    }

    private fun favoriteArticle(id: Int) {
        viewModelScope.launch {
            favoriteArticleUseCase(id).onStart {
                mFavoriteState.emit(UiState.Loading)
            }.onEach {
                mFavoriteState.emit(UiState.Success(true))
            }.catch {
                mFavoriteState.emit(UiState.Exception(it))
            }.launchIn(viewModelScope)
        }
    }

    private fun cancelFavoriteArticle(id: Int) {
        viewModelScope.launch {
            cancelFavoriteArticleUseCase(id).onStart {
                mFavoriteState.emit(UiState.Loading)
            }.onEach {
                mFavoriteState.emit(UiState.Success(true))
            }.catch {
                mFavoriteState.emit(UiState.Exception(it))
            }.launchIn(viewModelScope)
        }
    }

}