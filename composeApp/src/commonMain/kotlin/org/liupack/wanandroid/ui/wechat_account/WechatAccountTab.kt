package org.liupack.wanandroid.ui.wechat_account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.liupack.wanandroid.router.Router

fun RouteBuilder.wechatAccountScreen(navigator: Navigator) {
    scene(route = Router.WechatAccount.path,navTransition = NavTransition()) {
        WechatAccountScreen(navigator)
    }
}

@Composable
private fun WechatAccountScreen(navigator: Navigator) {
    val viewModel = koinViewModel(WechatAccountViewModel::class)
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("公众号${viewModel.hashCode()}")
    }
}