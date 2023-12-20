package org.liupack.wanandroid.ui.system

import androidx.compose.runtime.Composable
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator

@Composable
fun SystemScreen(navigator: Navigator = rememberNavigator()) {
    val viewModel = koinViewModel(SystemViewModel::class)

}