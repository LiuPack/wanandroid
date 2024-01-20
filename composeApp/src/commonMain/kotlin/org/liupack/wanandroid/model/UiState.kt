package org.liupack.wanandroid.model

import org.liupack.wanandroid.network.exception.LoginExpiredException

typealias UiStateLoading = UiState.Loading
typealias UiStateException = UiState.Exception
typealias UiStateFailed = UiState.Failed
typealias UiStateSuccess<T> = UiState.Success<T>

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()

    data class Exception(val throwable: Throwable) : UiState<Nothing>()

    data class Failed(val message: String) : UiState<Nothing>()

    data class Success<T>(val `data`: T) : UiState<T>()

    companion object {

        val <T> UiState<T>.isLoginExpired get() = this is Exception && this.throwable is LoginExpiredException

        val <T> UiState<T>.successDataOrNull get() = if (this is Success) data else null
    }
}