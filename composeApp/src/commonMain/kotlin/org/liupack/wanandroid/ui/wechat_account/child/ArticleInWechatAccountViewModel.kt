package org.liupack.wanandroid.ui.wechat_account.child

import androidx.paging.cachedIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.liupack.wanandroid.model.Repository
import org.liupack.wanandroid.model.usecase.CancelFavoriteArticleUseCase
import org.liupack.wanandroid.model.usecase.FavoriteArticleUseCase

class ArticleInWechatAccountViewModel(
    private val id: Int,
    private val repository: Repository,
    private val favoriteArticleUseCase: FavoriteArticleUseCase,
    private val cancelFavoriteArticleUseCase: CancelFavoriteArticleUseCase,
) : ViewModel() {

    val article = repository.articleInWechatAccount(id).cachedIn(viewModelScope)

    private var mFavoriteState = MutableStateFlow(false)
    val favoriteState = mFavoriteState.asStateFlow()

    private var mCancelFavoriteState = MutableStateFlow(false)
    val cancelFavoriteState = mCancelFavoriteState.asStateFlow()

    fun dispatch(action: ArticleInWechatAccountAction) {
        when (action) {

            is ArticleInWechatAccountAction.Refresh -> {
            }

            is ArticleInWechatAccountAction.Favorite -> {
                favoriteArticle(action.id)
            }

            is ArticleInWechatAccountAction.CancelFavorite -> {
                cancelFavoriteArticle(action.id)
            }
        }
    }

    private fun favoriteArticle(id: Int) {
        viewModelScope.launch {
            favoriteArticleUseCase(id).onStart {
                mFavoriteState.emit(false)
            }.catch {
                mFavoriteState.emit(false)
            }.collectLatest {
                mFavoriteState.emit(true)
            }
        }
    }

    private fun cancelFavoriteArticle(id: Int) {
        viewModelScope.launch {
            cancelFavoriteArticleUseCase(id).onStart {
                mCancelFavoriteState.emit(false)
            }.catch {
                mCancelFavoriteState.emit(false)
            }.collectLatest {
                mCancelFavoriteState.emit(true)
            }
        }
    }
}
