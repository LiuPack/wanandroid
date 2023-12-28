package org.liupack.wanandroid.ui.user

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.liupack.wanandroid.model.Repository
import org.liupack.wanandroid.model.UiState
import org.liupack.wanandroid.model.entity.UserFullInfoData
import org.liupack.wanandroid.network.exception.DataResultException

class UserViewModel(private val repository: Repository) : ScreenModel {

    private val mUserInfo = MutableStateFlow<UiState<UserFullInfoData?>>(UiState.Loading)
    val userInfo = mUserInfo.asStateFlow()

    fun userInfo() {
        screenModelScope.launch {
            repository.userInfo().catch {
                if (it is DataResultException) {
                    mUserInfo.emit(UiState.Failed(it.message))
                } else {
                    mUserInfo.emit(UiState.Exception(it))
                }
            }.collectLatest {
                mUserInfo.emit(UiState.Success(it))
            }
        }
    }

    override fun onDispose() {
        super.onDispose()
        screenModelScope.cancel()
    }
}
