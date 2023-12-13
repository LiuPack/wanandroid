package org.liupack.wanandroid.ui.login

sealed class LoginAction {
    data class UserNameInput(val userName: String) : LoginAction()
    data class PasswordInput(val password: String) : LoginAction()
    data class Login(val userName: String, val password: String) : LoginAction()
}