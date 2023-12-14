package org.liupack.wanandroid.ui.register

sealed class RegisterAction {
    data class InputUserName(val userName: String) : RegisterAction()
    data class InputPassword(val password: String) : RegisterAction()

    data class InputRePassword(val rePassword: String) : RegisterAction()

    data class Register(val userName: String, val password: String, val rePassword: String) :
        RegisterAction()
}