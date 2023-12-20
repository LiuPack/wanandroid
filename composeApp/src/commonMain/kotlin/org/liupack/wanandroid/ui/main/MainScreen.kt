package org.liupack.wanandroid.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.BackStackEntry
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.rememberNavigator
import org.liupack.wanandroid.router.Router
import org.liupack.wanandroid.ui.home.HomeScreen
import org.liupack.wanandroid.ui.login.LoginScreen
import org.liupack.wanandroid.ui.project.ProjectScreen
import org.liupack.wanandroid.ui.register.RegisterScreen
import org.liupack.wanandroid.ui.system.SystemScreen
import org.liupack.wanandroid.ui.user.UserScreen
import org.liupack.wanandroid.ui.wechat_account.WechatAccountScreen

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MainScreen() {
    val navigator = rememberNavigator()
    val windowSizeClass = calculateWindowSizeClass()
    val currentBackStackEntry by navigator.currentEntry.collectAsStateWithLifecycle(null)
    val viewModel = koinViewModel(MainViewModel::class)
    var inMain by rememberSaveable(viewModel) { mutableStateOf(true) }
    LaunchedEffect(currentBackStackEntry) {
        inMain = currentBackStackEntry?.path in viewModel.mainRoute
    }
    if (windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact) {
        Row(modifier = Modifier.fillMaxSize()) {
            AnimatedShowBottomNavigationBar(
                inMain = inMain,
                currentBackStackEntry = currentBackStackEntry,
                navigator = navigator,
                viewModel = viewModel,
                isHorizontal = true
            )
            NavContent(navigator = navigator, modifier = Modifier.weight(1f))
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            NavContent(navigator = navigator, modifier = Modifier.weight(1f))
            AnimatedShowBottomNavigationBar(
                inMain = inMain,
                currentBackStackEntry = currentBackStackEntry,
                navigator = navigator,
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun NavContent(navigator: Navigator = rememberNavigator(), modifier: Modifier) {
    NavHost(modifier = modifier,
        navigator = navigator,
        initialRoute = Router.Home.path,
        builder = {
            scene(Router.Login.path) {
                LoginScreen(navigator)
            }
            scene(Router.Register.path) {
                RegisterScreen(navigator)
            }
            scene(Router.Home.path) {
                HomeScreen(navigator)
            }
            scene(Router.System.path) {
                SystemScreen(navigator)
            }
            scene(Router.WechatAccount.path) {
                WechatAccountScreen(navigator)
            }
            scene(Router.Project.path) {
                ProjectScreen(navigator)
            }
            scene(Router.User.path) {
                UserScreen(navigator)
            }
        })
}

@Composable
private fun AnimatedShowBottomNavigationBar(
    inMain: Boolean,
    currentBackStackEntry: BackStackEntry? = null,
    navigator: Navigator = rememberNavigator(),
    viewModel: MainViewModel = koinViewModel(MainViewModel::class),
    isHorizontal: Boolean = false,
) {
    if (isHorizontal) {
        AnimatedVisibility(inMain, enter = expandHorizontally(), exit = shrinkHorizontally()) {
            NavigationRail(modifier = Modifier.fillMaxHeight()) {
                viewModel.navigators.forEach {
                    val selected = currentBackStackEntry?.path == it.router.path
                    NavigationRailItem(selected = selected, icon = {
                        Image(imageVector = it.icon, contentDescription = null)
                    }, label = {
                        Text(text = it.label)
                    }, onClick = {
                        navigator.navigate(
                            route = it.router.path,
                            options = NavOptions(launchSingleTop = true, popUpTo = PopUpTo.First())
                        )
                    })
                }
            }
        }
    } else {
        AnimatedVisibility(inMain, enter = expandVertically(), exit = shrinkVertically()) {
            NavigationBar(modifier = Modifier.fillMaxWidth()) {
                viewModel.navigators.forEach {
                    val selected = currentBackStackEntry?.path == it.router.path
                    NavigationBarItem(selected = selected, icon = {
                        Image(imageVector = it.icon, contentDescription = null)
                    }, label = {
                        Text(text = it.label)
                    }, onClick = {
                        navigator.navigate(
                            route = it.router.path,
                            options = NavOptions(launchSingleTop = true, popUpTo = PopUpTo.First())
                        )
                    })
                }
            }
        }
    }
}