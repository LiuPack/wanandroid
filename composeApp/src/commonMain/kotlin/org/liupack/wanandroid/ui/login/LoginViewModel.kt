package org.liupack.wanandroid.ui.login

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.liupack.wanandroid.model.Repository

class LoginViewModel(private val repository: Repository) : ViewModel() {

    private val mUserNameInput = MutableStateFlow("")
    val userNameInput = mUserNameInput.asStateFlow()

    private val mPasswordInput = MutableStateFlow("")
    val passwordInput = mPasswordInput.asStateFlow()

    fun dispatch(action: LoginAction) {
        viewModelScope.launch {
            when (action) {
                is LoginAction.Login -> {

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