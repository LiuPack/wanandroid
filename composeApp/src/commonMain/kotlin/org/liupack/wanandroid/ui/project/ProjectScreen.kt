package org.liupack.wanandroid.ui.project

import androidx.compose.runtime.Composable
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator

@Composable
fun ProjectScreen(navigator: Navigator) {
    val viewModel = koinViewModel(ProjectViewModel::class)
}