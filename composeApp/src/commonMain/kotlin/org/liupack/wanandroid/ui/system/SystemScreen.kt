package org.liupack.wanandroid.ui.system

import androidx.compose.runtime.Composable
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator

@Composable
fun SystemScreen(navigator: Navigator) {
    val viewModel = koinViewModel(SystemViewModel::class)
}