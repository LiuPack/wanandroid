package org.liupack.wanandroid.ui.register

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
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.liupack.wanandroid.common.Constants
import org.liupack.wanandroid.model.Repository
import org.liupack.wanandroid.model.UiState
import org.liupack.wanandroid.model.entity.UserInfoData
import org.liupack.wanandroid.network.exception.DataResultException
import org.liupack.wanandroid.platform.settings

class RegisterViewModel(private val repository: Repository) : ViewModel() {

    private val mInputUserName = MutableStateFlow("")
    val inputUserName = mInputUserName.asStateFlow()

    private val mInputPassword = MutableStateFlow("")
    val inputPassword = mInputPassword.asStateFlow()

    private val mInputRePassword = MutableStateFlow("")
    val inputRePassword = mInputRePassword.asStateFlow()

    private val mRegisterState = MutableSharedFlow<UiState<UserInfoData?>>()
    val registerState = mRegisterState.asSharedFlow()
    fun dispatch(action: RegisterAction) {
        viewModelScope.launch {
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
        viewModelScope.launch {
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
        viewModelScope.launch {
            mInputUserName.update { userName }
        }
    }

    private fun inputRePassword(rePassword: String) {
        viewModelScope.launch {
            mInputRePassword.update { rePassword }
        }
    }

    private fun inputPassword(password: String) {
        viewModelScope.launch {
            mInputPassword.update { password }
        }
    }
}
