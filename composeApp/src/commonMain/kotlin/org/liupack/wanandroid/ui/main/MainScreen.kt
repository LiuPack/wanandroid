package org.liupack.wanandroid.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.BackStackEntry
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator
import org.liupack.wanandroid.composables.MessageDialog
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
import org.liupack.wanandroid.ui.user_favorite.userFavoriteScreen
import org.liupack.wanandroid.ui.user_shared.add_shared.userAddSharedScreen
import org.liupack.wanandroid.ui.user_shared.userSharedScreen
import org.liupack.wanandroid.ui.webview.webviewScreen
import org.liupack.wanandroid.ui.wechat_account.wechatAccountScreen


@Composable
fun MainScreen(windowSizeClass: WindowSizeClass) {
    val navigator = rememberNavigator()
    val viewModel = koinViewModel(MainViewModel::class)
    val currentEntry by navigator.currentEntry.collectAsState(null)
    val isNavigation =
        viewModel.bottomNavigatorDataLists.any { it.router.path == currentEntry?.path }
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            Column(modifier = Modifier.fillMaxSize()) {
                WindowContent(navigator = navigator, modifier = Modifier.weight(1f))
                AnimatedNavigation(
                    modifier = Modifier.fillMaxWidth(),
                    isNavigation = isNavigation,
                    viewModel = viewModel,
                    currentEntry = currentEntry,
                    navigator = navigator,
                    windowSizeClass = windowSizeClass,
                )
            }
        }

        else -> {
            Row(modifier = Modifier.fillMaxSize()) {
                AnimatedNavigation(
                    modifier = Modifier.fillMaxHeight().width(100.dp).zIndex(12f),
                    isNavigation = isNavigation,
                    viewModel = viewModel,
                    currentEntry = currentEntry,
                    navigator = navigator,
                    windowSizeClass = windowSizeClass,
                    enter = expandHorizontally(),
                    exit = shrinkHorizontally(),
                )
                WindowContent(navigator = navigator, modifier = Modifier.weight(1f))
            }
        }
    }
    MessageDialog(true, windowSizeClass.widthSizeClass.toString())
}

@Composable
private fun AnimatedNavigation(
    modifier: Modifier = Modifier.fillMaxWidth(),
    isNavigation: Boolean,
    viewModel: MainViewModel,
    currentEntry: BackStackEntry?,
    navigator: Navigator,
    windowSizeClass: WindowSizeClass,
    enter: EnterTransition = expandVertically(),
    exit: ExitTransition = shrinkVertically(),
) {
    AnimatedVisibility(
        visible = isNavigation,
        enter = enter,
        exit = exit,
        modifier = modifier
    ) {
        if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
            BottomAppBar(
                modifier = modifier,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = contentColorFor(MaterialTheme.colorScheme.background)
            ) {
                viewModel.bottomNavigatorDataLists.forEach {
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
        } else {
            NavigationRail(
                modifier = modifier,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = contentColorFor(MaterialTheme.colorScheme.background)
            ) {
                viewModel.bottomNavigatorDataLists.forEach {
                    NavigationRailItem(selected = currentEntry?.path == it.router.path, onClick = {
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

@Composable
private fun WindowContent(navigator: Navigator, modifier: Modifier = Modifier) {
    NavHost(navigator = navigator,
        initialRoute = Router.Splash.path,
        modifier = modifier,
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
            userFavoriteScreen(navigator = navigator)
            userSharedScreen(navigator = navigator)
            userAddSharedScreen(navigator = navigator)
        })
}
