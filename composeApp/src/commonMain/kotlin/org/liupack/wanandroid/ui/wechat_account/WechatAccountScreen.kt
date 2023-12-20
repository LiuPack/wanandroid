package org.liupack.wanandroid.ui.wechat_account

import androidx.compose.runtime.Composable
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator

@Composable
fun WechatAccountScreen(navigator: Navigator = rememberNavigator()) {
    val viewModel = koinViewModel(WechatAccountViewModel::class)
}