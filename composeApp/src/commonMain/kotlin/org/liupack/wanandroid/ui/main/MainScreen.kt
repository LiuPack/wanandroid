package org.liupack.wanandroid.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.rememberNavigator
import org.liupack.wanandroid.router.Router
import org.liupack.wanandroid.ui.coin_count_ranking.coinCountRankingScreen
import org.liupack.wanandroid.ui.home.homeScreen
import org.liupack.wanandroid.ui.login.loginScreen
import org.liupack.wanandroid.ui.project.projectScreen
import org.liupack.wanandroid.ui.register.registerScreen
import org.liupack.wanandroid.ui.setting.settingScreen
import org.liupack.wanandroid.ui.splash.splashScreen
import org.liupack.wanandroid.ui.system.articles_in_system.articleInSystemScreen
import org.liupack.wanandroid.ui.system.systemScreen
import org.liupack.wanandroid.ui.user.userScreen
import org.liupack.wanandroid.ui.user_coincount.userCoinCountScreen
import org.liupack.wanandroid.ui.webview.webviewScreen
import org.liupack.wanandroid.ui.wechat_account.wechatAccountScreen


@Composable
fun MainScreen() {
    val navigator = rememberNavigator()
    val viewModel = koinViewModel(MainViewModel::class)
    val currentEntry by navigator.currentEntry.collectAsState(null)
    val isNavigation = viewModel.navigationList.any { it.router.path == currentEntry?.path }
    Column(modifier = Modifier.fillMaxSize()) {
        NavHost(navigator = navigator,
            initialRoute = Router.Splash.path,
            modifier = Modifier.weight(1f),
            persistNavState = true,
            builder = {
                splashScreen(navigator = navigator)
                homeScreen(navigator = navigator)
                systemScreen(navigator = navigator)
                articleInSystemScreen(navigator = navigator)
                wechatAccountScreen(navigator = navigator)
                projectScreen(navigator = navigator)
                userScreen(navigator = navigator)
                coinCountRankingScreen(navigator = navigator)
                loginScreen(navigator = navigator)
                registerScreen(navigator = navigator)
                userCoinCountScreen(navigator = navigator)
                webviewScreen(navigator = navigator)
                settingScreen(navigator = navigator)
            })
        AnimatedVisibility(
            visible = isNavigation,
            enter = expandVertically(),
            exit = shrinkVertically(),
            modifier = Modifier.fillMaxWidth()
        ) {
            BottomAppBar(modifier = Modifier.fillMaxWidth()) {
                viewModel.navigationList.forEach {
                    NavigationBarItem(selected = currentEntry?.path == it.router.path, onClick = {
                        navigator.navigate(
                            route = it.router.path, options = NavOptions(launchSingleTop = true)
                        )
                    }, icon = {
                        Icon(imageVector = it.icon, contentDescription = null)
                    }, label = {
                        Text(text = it.label)
                    })
                }
            }
        }
    }
}
