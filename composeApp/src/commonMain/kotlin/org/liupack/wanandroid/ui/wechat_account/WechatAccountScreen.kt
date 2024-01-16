package org.liupack.wanandroid.ui.wechat_account

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.BackHandler
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.liupack.wanandroid.platform.exitApp
import org.liupack.wanandroid.router.Router

fun RouteBuilder.wechatAccountScreen(navigator: Navigator) {
    scene(route = Router.WechatAccount.path, navTransition = NavTransition()) {
        BackHandler { exitApp() }
        WechatAccountScreen(navigator)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WechatAccountScreen(navigator: Navigator) {
    val viewModel = koinViewModel(WechatAccountViewModel::class)
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("公众号") }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            )
        )
    }) {

    }
}