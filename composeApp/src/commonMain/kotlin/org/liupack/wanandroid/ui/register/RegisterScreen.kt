package org.liupack.wanandroid.ui.register

import androidx.compose.runtime.Composable
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun RegisterScreen() {
    val viewModel = koinViewModel(RegisterViewModel::class)
}