package org.liupack.wanandroid.ui.user

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
import org.liupack.wanandroid.common.Constants
import org.liupack.wanandroid.model.Repository
import org.liupack.wanandroid.model.UiState
import org.liupack.wanandroid.model.entity.UserFullInfoData
import org.liupack.wanandroid.model.entity.UserNavigator
import org.liupack.wanandroid.network.exception.DataResultException
import org.liupack.wanandroid.platform.settings

class UserViewModel(private val repository: Repository) : ViewModel() {

    private val mUserInfoState = MutableStateFlow<UiState<UserFullInfoData?>>(UiState.Loading)
    val userInfoState = mUserInfoState.asStateFlow()
    private val mToLogin = MutableSharedFlow<Boolean>()
    val toLogin = mToLogin.asSharedFlow()
    private val mToLogout = MutableSharedFlow<UiState<Boolean>>()
    val logout = mToLogout.asSharedFlow()

    val userNavigator = listOf(
        UserNavigator.UserCoinCount,
        UserNavigator.UserShared,
        UserNavigator.UserCollect,
        UserNavigator.AboutUser,
        UserNavigator.SystemSetting,
        UserNavigator.Logout
    )

    fun dispatch(action: UserAction) {
        when (action) {
            UserAction.Login -> {
                toLogin()
            }

            UserAction.Logout -> {
                logout()
            }

            UserAction.Refresh -> {
                userInfo()
            }
        }
    }

    private fun toLogin() {
        viewModelScope.launch {
            mToLogin.emit(true)
        }
    }

    private fun logout() {
        viewModelScope.launch {
            repository.logout().onStart {
                mToLogout.emit(UiState.Loading)
            }.catch {
                if (it is DataResultException) {
                    mToLogout.emit(UiState.Failed(it.message))
                } else {
                    mToLogout.emit(UiState.Exception(it))
                }
            }.collectLatest {
                settings.remove(Constants.loginUserName)
                settings.remove(Constants.loginUserPassword)
                mToLogout.emit(UiState.Success(true))
            }
        }
    }

    private fun userInfo() {
        viewModelScope.launch {
            repository.userInfo().catch {
                if (it is DataResultException) {
                    mUserInfoState.emit(UiState.Failed(it.message))
                } else {
                    mUserInfoState.emit(UiState.Exception(it))
                }
            }.collectLatest {
                mUserInfoState.emit(UiState.Success(it))
            }
        }
    }
}
