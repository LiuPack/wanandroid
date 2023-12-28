package org.liupack.wanandroid.ui.login

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.ktor.util.encodeBase64
import kotlinx.coroutines.cancel
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
import org.liupack.wanandroid.common.Constants
import org.liupack.wanandroid.common.Logger
import org.liupack.wanandroid.model.Repository
import org.liupack.wanandroid.model.UiState
import org.liupack.wanandroid.model.entity.UserInfoData
import org.liupack.wanandroid.network.exception.DataResultException
import org.liupack.wanandroid.platform.settings

class LoginViewModel(private val repository: Repository) : ScreenModel {

    private val mUserNameInput =
        MutableStateFlow(settings.getStringOrNull(Constants.loginUserName).orEmpty())
    val userNameInput = mUserNameInput.asStateFlow()

    private val mPasswordInput =
        MutableStateFlow(settings.getStringOrNull(Constants.loginUserPassword).orEmpty())
    val passwordInput = mPasswordInput.asStateFlow()

    private val mLoginState = MutableSharedFlow<UiState<UserInfoData?>>()
    val loginState = mLoginState.asSharedFlow()

    fun dispatch(action: LoginAction) {
        screenModelScope.launch {
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
        screenModelScope.launch {
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
        screenModelScope.launch {
            mUserNameInput.update { userName }
        }
    }

    private fun passwordUpdate(password: String) {
        screenModelScope.launch {
            mPasswordInput.update { password }
        }
    }

    override fun onDispose() {
        super.onDispose()
        screenModelScope.cancel()
    }
}