package org.liupack.wanandroid.ui.user_favorite

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
import org.liupack.wanandroid.model.usecase.CancelUserFavoriteArticleUseCase

class UserFavoriteViewModel(
    private val repository: Repository,
    private val cancelUserFavoriteArticleUseCase: CancelUserFavoriteArticleUseCase
) : ViewModel() {

    val favoriteArticles = repository.userFavoriteArticles().cachedIn(viewModelScope)

    private val mFavoriteState = MutableStateFlow<Boolean?>(null)
    val favoriteState = mFavoriteState.asStateFlow()

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
                mFavoriteState.emit(null)
            }.onEach {
                mFavoriteState.emit(true)
            }.catch {
                mFavoriteState.emit(false)
            }.launchIn(viewModelScope)
        }
    }

}