package org.liupack.wanandroid.ui.user_shared.add_shared

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.liupack.wanandroid.model.Repository
import org.liupack.wanandroid.model.UiState
import org.liupack.wanandroid.network.exception.DataResultException

class UserAddSharedViewModel(private val repository: Repository) : ViewModel() {

    private val mShareTitle = MutableStateFlow("")
    val shareTitle = mShareTitle.asStateFlow()

    private val mShareLink = MutableStateFlow("")
    val shareLink = mShareLink.asStateFlow()

    private val mSubmitShared = MutableSharedFlow<UiState<String>>()
    val submitShared = mSubmitShared.asSharedFlow()

    fun dispatch(action: UserAddSharedAction) {
        viewModelScope.launch {
            when (action) {
                is UserAddSharedAction.InputLink -> {
                    inputLinkUpdate(action.link)
                }

                is UserAddSharedAction.InputTitle -> {
                    inputTitleUpdate(action.title)
                }

                is UserAddSharedAction.Shared -> {
                    submitShared(shareTitle.value, shareLink.value)
                }
            }
        }
    }

    private fun inputTitleUpdate(title: String) {
        viewModelScope.launch {
            mShareTitle.emit(title)
        }
    }

    private fun inputLinkUpdate(link: String) {
        viewModelScope.launch {
            mShareLink.emit(link)
        }
    }

    private fun submitShared(title: String, link: String) {
        viewModelScope.launch {
            if (title.isEmpty()) {
                mSubmitShared.emit(UiState.Failed("请输入文章标题"))
                return@launch
            }
            if (link.isEmpty()) {
                mSubmitShared.emit(UiState.Failed("请输入文章链接"))
                return@launch
            }
            repository.userAddSharedArticle(title, link).onStart {
                mSubmitShared.emit(UiState.Loading)
            }.catch {
                if (it is DataResultException) {
                    mSubmitShared.emit(UiState.Failed(it.message))
                } else {
                    mSubmitShared.emit(UiState.Exception(it))
                }
            }.collectLatest {
                mSubmitShared.emit(UiState.Success("提交成功"))
            }
        }
    }
}