package org.liupack.wanandroid.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import org.liupack.wanandroid.ui.home.HomeTab
import org.liupack.wanandroid.ui.project.ProjectTab
import org.liupack.wanandroid.ui.system.SystemTab
import org.liupack.wanandroid.ui.user.UserTab
import org.liupack.wanandroid.ui.wechat_account.WechatAccountTab


internal val LocalParentNavigator
    @Composable
    get() = LocalNavigator.currentOrThrow.parent ?: LocalNavigator.currentOrThrow

data object MainScreen : Screen {
    @Composable
    override fun Content() {
        TabNavigatorContent()
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Composable
    private fun TabNavigatorContent() {
        TabNavigator(HomeTab) {
            val windowWidthSizeClass = calculateWindowSizeClass()
            when (windowWidthSizeClass.widthSizeClass) {
                WindowWidthSizeClass.Compact -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Box(modifier = Modifier.weight(1f)) {
                            CurrentTab()
                        }
                        NavigationBar(modifier = Modifier.fillMaxWidth()) {
                            TabNavigatorItem(HomeTab)
                            TabNavigatorItem(SystemTab)
                            TabNavigatorItem(WechatAccountTab)
                            TabNavigatorItem(ProjectTab)
                            TabNavigatorItem(UserTab)
                        }
                    }
                }

                else -> {

                    Row(modifier = Modifier.fillMaxSize()) {
                        NavigationRail(modifier = Modifier.fillMaxHeight()) {
                            TabNavigatorRailItem(HomeTab)
                            TabNavigatorRailItem(SystemTab)
                            TabNavigatorRailItem(WechatAccountTab)
                            TabNavigatorRailItem(ProjectTab)
                            TabNavigatorRailItem(UserTab)
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            CurrentTab()
                        }
                    }
                }
            }

        }
    }

    @Composable
    private fun TabNavigatorRailItem(tab: Tab = HomeTab) {
        val tabNavigator = LocalTabNavigator.current
        NavigationRailItem(
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

    @Composable
    private fun RowScope.TabNavigatorItem(tab: Tab = HomeTab) {
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
