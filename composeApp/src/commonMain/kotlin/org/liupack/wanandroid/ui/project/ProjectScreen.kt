package org.liupack.wanandroid.ui.project

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.BackHandler
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.liupack.wanandroid.platform.exitApp
import org.liupack.wanandroid.router.Router

fun RouteBuilder.projectScreen(navigator: Navigator) {
    scene(route = Router.Project.path,navTransition = NavTransition()) {
        BackHandler { exitApp() }
        ProjectScreen(navigator = navigator)
    }
}

@Composable
private fun ProjectScreen(navigator: Navigator) {
    val viewModel = koinViewModel(ProjectViewModel::class)
    LaunchedEffect(viewModel.hashCode()) {
        viewModel.projectSort()
    }
    val projectSortList by viewModel.projectSort.collectAsState()
    if (projectSortList.isNotEmpty()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
//                TopAppBar(title = {
//                    ScrollableTabRow(
//                        edgePadding = 0.dp,
//                        divider = {},
//                        indicator = {
//                            TabRowDefaults.Indicator(
//                                Modifier.tabIndicatorOffset(it[tabNavigator.current.options.index.toInt()])
//                                    .padding(horizontal = 24.dp),
//                                height = 2.dp
//                            )
//                        },
//                        selectedTabIndex = tabNavigator.current.options.index.toInt(),
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        tabs.forEach {
//                            TabItem(it)
//                        }
//                    }
//                })
            },
            content = { paddingValues ->

            })
    }
}