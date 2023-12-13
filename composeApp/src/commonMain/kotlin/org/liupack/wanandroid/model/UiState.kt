package org.liupack.wanandroid.model

sealed class UiState<T> {
    data object Loading : UiState<Nothing>()

    data class Exception(val throwable: Throwable) : UiState<Nothing>()

    data class Failed(val message: String) : UiState<Nothing>()

    data class Success<T>(val `data`: T) : UiState<T>()

}