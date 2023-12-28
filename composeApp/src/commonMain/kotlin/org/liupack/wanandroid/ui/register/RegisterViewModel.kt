package org.liupack.wanandroid.ui.register

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
import org.liupack.wanandroid.common.Constants
import org.liupack.wanandroid.model.Repository
import org.liupack.wanandroid.model.UiState
import org.liupack.wanandroid.model.entity.UserInfoData
import org.liupack.wanandroid.network.exception.DataResultException
import org.liupack.wanandroid.platform.settings

class RegisterViewModel(private val repository: Repository) : ScreenModel {

    private val mInputUserName = MutableStateFlow("")
    val inputUserName = mInputUserName.asStateFlow()

    private val mInputPassword = MutableStateFlow("")
    val inputPassword = mInputPassword.asStateFlow()

    private val mInputRePassword = MutableStateFlow("")
    val inputRePassword = mInputRePassword.asStateFlow()

    private val mRegisterState = MutableSharedFlow<UiState<UserInfoData?>>()
    val registerState = mRegisterState.asSharedFlow()
    fun dispatch(action: RegisterAction) {
        screenModelScope.launch {
            when (action) {
                is RegisterAction.InputPassword -> {
                    inputPassword(action.password)
                }

                is RegisterAction.InputRePassword -> {
                    inputRePassword(action.rePassword)
                }

                is RegisterAction.InputUserName -> {
                    inputUserName(action.userName)
                }

                is RegisterAction.Register -> {
                    register(action.userName, action.password, action.rePassword)
                }
            }
        }
    }

    private fun register(userName: String, password: String, rePassword: String) {
        screenModelScope.launch {
            repository.register(userName, password, rePassword).onStart {
                mRegisterState.emit(UiState.Loading)
            }.catch {
                if (it is DataResultException) {
                    mRegisterState.emit(UiState.Failed(it.message))
                } else {
                    mRegisterState.emit(UiState.Exception(it))
                }
            }.collectLatest {
                settings.putBoolean(Constants.isLogin, true)
                settings.putString(Constants.loginUserName, userName.encodeBase64())
                settings.putString(Constants.loginUserPassword, password.encodeBase64())
                mRegisterState.emit(UiState.Success(it))
            }
        }
    }

    private fun inputUserName(userName: String) {
        screenModelScope.launch {
            mInputUserName.update { userName }
        }
    }

    private fun inputRePassword(rePassword: String) {
        screenModelScope.launch {
            mInputRePassword.update { rePassword }
        }
    }

    private fun inputPassword(password: String) {
        screenModelScope.launch {
            mInputPassword.update { password }
        }
    }

    override fun onDispose() {
        super.onDispose()
        screenModelScope.cancel()
    }
}
