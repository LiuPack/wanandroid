package org.liupack.wanandroid.ui.user_favorite

import androidx.paging.cachedIn
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.liupack.wanandroid.model.Repository
import org.liupack.wanandroid.model.UiState
import org.liupack.wanandroid.model.usecase.CancelUserFavoriteArticleUseCase

class UserFavoriteViewModel(
    private val repository: Repository,
    private val cancelUserFavoriteArticleUseCase: CancelUserFavoriteArticleUseCase
) : ViewModel() {

    val favoriteArticles = repository.userFavoriteArticles().cachedIn(viewModelScope)

    private val mFavoriteState = MutableSharedFlow<UiState<Boolean>>()
    val favoriteState = mFavoriteState.asSharedFlow()

    fun dispatch(action: UserFavoriteAction) {
        viewModelScope.launch {
            when (action) {
                is UserFavoriteAction.CancelFavorite -> {
                    cancelFavoriteArticle(id = action.id, originId = action.originId)
                }
            }
        }
    }

    private fun cancelFavoriteArticle(id: Int, originId: Int) {
        viewModelScope.launch {
            cancelUserFavoriteArticleUseCase(id = id, originId = originId).onStart {
                mFavoriteState.emit(UiState.Loading)
            }.onEach {
                mFavoriteState.emit(UiState.Success(true))
            }.catch {
                mFavoriteState.emit(UiState.Exception(it))
            }.launchIn(viewModelScope)
        }
    }

}