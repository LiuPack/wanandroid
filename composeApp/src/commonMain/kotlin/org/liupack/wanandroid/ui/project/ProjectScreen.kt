package org.liupack.wanandroid.ui.project

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.BackHandler
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.liupack.wanandroid.platform.exitApp
import org.liupack.wanandroid.router.Router
import org.liupack.wanandroid.ui.project.child.projectListScreen

fun RouteBuilder.projectScreen(navigator: Navigator) {
    scene(route = Router.Project.path, navTransition = NavTransition()) {
        BackHandler { exitApp() }
        ProjectScreen(navigator = navigator)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProjectScreen(navigator: Navigator) {
    val viewModel = koinViewModel(ProjectViewModel::class)
    LaunchedEffect(viewModel) {
        viewModel.projectSort()
    }
    val projectSortList by viewModel.projectSort.collectAsState()
    if (projectSortList.isNotEmpty()) {
        val childNavigator = rememberNavigator()
        val routers by remember(viewModel) {
            derivedStateOf {
                projectSortList.map { it.id.toString() }.toList()
            }
        }
        val selectIndex by viewModel.selectIndex.collectAsState()
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopAppBar(
                    title = {
                        ScrollableTabRow(
                            selectedTabIndex = selectIndex,
                            modifier = Modifier.padding(end = 16.dp).fillMaxWidth()
                                .clip(MaterialTheme.shapes.medium),
                            edgePadding = 0.dp,
                            divider = {},
                            indicator = {},
                            tabs = {
                                projectSortList.forEachIndexed { index, data ->
                                    Box(
                                        modifier = Modifier.fillMaxWidth()
                                            .clip(MaterialTheme.shapes.medium)
                                            .clickable {
                                                viewModel.updateSelected(index)
                                                childNavigator.navigate(
                                                    routers[index],
                                                    options = NavOptions(launchSingleTop = true)
                                                )
                                            }.background(
                                                MaterialTheme.colorScheme.inversePrimary.copy(if (index == selectIndex) 0.3f else 0f),
                                                MaterialTheme.shapes.medium
                                            ).padding(horizontal = 12.dp, vertical = 6.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = data.name,
                                            fontSize = MaterialTheme.typography.titleMedium.fontSize
                                        )
                                    }
                                }
                            }
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = contentColorFor(MaterialTheme.colorScheme.background),
                        actionIconContentColor = contentColorFor(MaterialTheme.colorScheme.background),
                        navigationIconContentColor = contentColorFor(MaterialTheme.colorScheme.background),
                    ),
                )
            },
            content = { paddingValues ->
                NavHost(
                    navigator = childNavigator,
                    initialRoute = routers.first(),
                    modifier = Modifier.fillMaxSize()
                        .padding(top = paddingValues.calculateTopPadding()),
                    navTransition = NavTransition(
                        createTransition = fadeIn(),
                        destroyTransition = fadeOut()
                    ),
                    persistNavState = true,
                    builder = {
                        routers.forEach {
                            projectListScreen(
                                parentNavigator = navigator,
                                router = it,
                                id = it.toInt()
                            )
                        }
                    })
            })
    }
}