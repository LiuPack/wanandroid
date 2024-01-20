package org.liupack.wanandroid.ui.user_shared

import androidx.paging.cachedIn
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.liupack.wanandroid.model.Repository
import org.liupack.wanandroid.model.UiState
import org.liupack.wanandroid.network.exception.DataResultException

class UserSharedViewModel(private val repository: Repository) : ViewModel() {

    val shareArticles = repository.userShareArticles().cachedIn(viewModelScope)

    private val mDeletedVisibility = MutableSharedFlow<Boolean>()
    val deletedVisibility = mDeletedVisibility.asSharedFlow()

    private val mDeletedState = MutableSharedFlow<UiState<String>>()
    val deletedState = mDeletedState.asSharedFlow()

    fun dispatch(action: UserSharedAction) {
        viewModelScope.launch {
            when (action) {
                is UserSharedAction.Deleted -> {
                    deleteShareArticle(action.id)
                }

                is UserSharedAction.ShowDeleted -> {
                    deletedVisibility(action.show)
                }
            }
        }
    }

    private fun deletedVisibility(isShow: Boolean) {
        viewModelScope.launch {
            mDeletedVisibility.emit(isShow)
        }
    }

    private fun deleteShareArticle(id: Int) {
        viewModelScope.launch {
            repository.deleteUserShareArticle(id).onStart {
                mDeletedState.emit(UiState.Loading)
            }.catch {
                if (it is DataResultException) {
                    mDeletedState.emit(UiState.Failed(it.message))
                } else {
                    mDeletedState.emit(UiState.Exception(it))
                }
            }.collectLatest {
                mDeletedState.emit(UiState.Success("删除成功"))
            }
        }
    }
}