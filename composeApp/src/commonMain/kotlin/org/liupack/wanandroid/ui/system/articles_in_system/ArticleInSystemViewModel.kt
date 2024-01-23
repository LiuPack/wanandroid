package org.liupack.wanandroid.ui.system.articles_in_system

import androidx.paging.cachedIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.liupack.wanandroid.model.Repository
import org.liupack.wanandroid.model.usecase.CancelFavoriteArticleUseCase
import org.liupack.wanandroid.model.usecase.FavoriteArticleUseCase

class ArticleInSystemViewModel(
    cid: Int,
    repository: Repository,
    private val favoriteArticleUseCase: FavoriteArticleUseCase,
    private val cancelFavoriteArticleUseCase: CancelFavoriteArticleUseCase,
) : ViewModel() {

    val articleState = repository.articleInSystem(cid).cachedIn(viewModelScope)

    private val mFavoriteState = MutableStateFlow<Boolean?>(null)
    val favoriteState = mFavoriteState.asStateFlow()

    fun dispatch(action: ArticleInSystemAction) {
        when (action) {
            is ArticleInSystemAction.CancelFavorite -> {
                cancelFavoriteArticle(action.id)
            }

            is ArticleInSystemAction.Favorite -> {
                favoriteArticle(action.id)
            }
        }
    }

    private fun favoriteArticle(id: Int) {
        viewModelScope.launch {
            favoriteArticleUseCase(id).onStart {
                mFavoriteState.emit(null)
            }.onEach {
                mFavoriteState.emit(true)
            }.catch {
                mFavoriteState.emit(false)
            }.launchIn(viewModelScope)
        }
    }

    private fun cancelFavoriteArticle(id: Int) {
        viewModelScope.launch {
            cancelFavoriteArticleUseCase(id).onStart {
                mFavoriteState.emit(null)
            }.onEach {
                mFavoriteState.emit(true)
            }.catch {
                mFavoriteState.emit(false)
            }.launchIn(viewModelScope)
        }
    }
}