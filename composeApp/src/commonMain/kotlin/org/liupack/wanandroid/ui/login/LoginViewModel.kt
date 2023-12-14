package org.liupack.wanandroid.ui.login

import io.ktor.util.encodeBase64
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.liupack.wanandroid.common.Constants
import org.liupack.wanandroid.common.Logger
import org.liupack.wanandroid.model.Repository
import org.liupack.wanandroid.model.UiState
import org.liupack.wanandroid.model.entity.UserInfoData
import org.liupack.wanandroid.network.exception.DataResultException
import org.liupack.wanandroid.platform.settings

class LoginViewModel(private val repository: Repository) : ViewModel() {

    private val mUserNameInput =
        MutableStateFlow(settings.getStringOrNull(Constants.loginUserName).orEmpty())
    val userNameInput = mUserNameInput.asStateFlow()

    private val mPasswordInput =
        MutableStateFlow(settings.getStringOrNull(Constants.loginUserPassword).orEmpty())
    val passwordInput = mPasswordInput.asStateFlow()

    private val mLoginState = MutableSharedFlow<UiState<UserInfoData?>>()
    val loginState = mLoginState.asSharedFlow()

    fun dispatch(action: LoginAction) {
        viewModelScope.launch {
            when (action) {
                is LoginAction.Login -> {
                    login(action.userName, action.password)
                }

                is LoginAction.PasswordInput -> {
                    passwordUpdate(action.password)
                }

                is LoginAction.UserNameInput -> {
                    userNameUpdate(action.userName)
                }
            }
        }
    }

    private fun login(userName: String, password: String) {
        viewModelScope.launch {
            repository.login(userName, password).onStart {
                mLoginState.emit(UiState.Loading)
            }.catch {
                if (it is DataResultException) {
                    mLoginState.emit(UiState.Failed(it.message))
                } else {
                    mLoginState.emit(UiState.Exception(it))
                }
            }.collectLatest {
                Logger.i(Json.encodeToString(it))
                settings.putBoolean(Constants.isLogin, true)
                settings.putString(Constants.loginUserName, userName.encodeBase64())
                settings.putString(Constants.loginUserPassword, password.encodeBase64())
                mLoginState.emit(UiState.Success(it))
            }
        }
    }

    private fun userNameUpdate(userName: String) {
        viewModelScope.launch {
            mUserNameInput.update { userName }
        }
    }

    private fun passwordUpdate(password: String) {
        viewModelScope.launch {
            mPasswordInput.update { password }
        }
    }
}