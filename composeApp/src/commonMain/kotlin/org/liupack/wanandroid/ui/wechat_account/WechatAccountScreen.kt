package org.liupack.wanandroid.ui.wechat_account

import androidx.compose.runtime.Composable
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator

@Composable
fun WechatAccountScreen(navigator: Navigator) {
    val viewModel = koinViewModel(WechatAccountViewModel::class)
}