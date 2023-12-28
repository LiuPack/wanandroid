package org.liupack.wanandroid.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabDisposable
import cafe.adriel.voyager.navigator.tab.TabNavigator
import org.liupack.wanandroid.ui.home.HomeScreen
import org.liupack.wanandroid.ui.project.ProjectScreen
import org.liupack.wanandroid.ui.system.SystemScreen
import org.liupack.wanandroid.ui.user.UserScreen
import org.liupack.wanandroid.ui.wechat_account.WechatAccountScreen

@Composable
fun MainScreen() {
    Navigator(screen = MainScreen)
}

object MainScreen : Screen {
    @Composable
    override fun Content() {
        TabNavigatorContent()
    }

    @Composable
    private fun TabNavigatorContent() {
        TabNavigator(HomeScreen, tabDisposable = {
            TabDisposable(
                navigator = it, tabs = listOf(
                    HomeScreen,
                    SystemScreen,
                    WechatAccountScreen,
                    ProjectScreen,
                    UserScreen
                )
            )
        }) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.weight(1f)) {
                    CurrentTab()
                }
                NavigationBar(modifier = Modifier.fillMaxWidth()) {
                    TabNavigatorItem(HomeScreen)
                    TabNavigatorItem(SystemScreen)
                    TabNavigatorItem(WechatAccountScreen)
                    TabNavigatorItem(ProjectScreen)
                    TabNavigatorItem(UserScreen)
                }
            }
        }
    }

    @Composable
    private fun RowScope.TabNavigatorItem(tab: Tab = HomeScreen) {
        val tabNavigator = LocalTabNavigator.current
        NavigationBarItem(
            selected = tabNavigator.current.key == tab.key,
            onClick = { tabNavigator.current = tab },
            label = { Text(tab.options.title) },
            icon = {
                tab.options.icon?.let { painter ->
                    Icon(painter, tab.options.title)
                }
            },
        )
    }
}
