package org.liupack.wanandroid.ui.project

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lt.compose_views.compose_pager.ComposePager
import com.lt.compose_views.compose_pager.rememberComposePagerState
import com.lt.compose_views.pager_indicator.TextPagerIndicator
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.BackHandler
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.liupack.wanandroid.platform.exitApp
import org.liupack.wanandroid.router.Router
import org.liupack.wanandroid.ui.project.child.ProjectListScreen

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
    LaunchedEffect(viewModel.hashCode()) {
        viewModel.projectSort()
    }
    val projectSortList by viewModel.projectSort.collectAsState()
    if (projectSortList.isNotEmpty()) {
        val pagerState = rememberComposePagerState()
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(title = {
                    TextPagerIndicator(
                        texts = projectSortList.map { it.name }.toList(),
                        offsetPercentWithSelectFlow = remember { pagerState.createChildOffsetPercentFlow() },
                        selectIndexFlow = remember { pagerState.createCurrSelectIndexFlow() },
                        fontSize = 16.sp,
                        selectFontSize = 20.sp,
                        textColor = MaterialTheme.colorScheme.onSurface,
                        selectTextColor = MaterialTheme.colorScheme.primary,
                        selectIndicatorColor = MaterialTheme.colorScheme.primary,
                        onIndicatorClick = {
                            pagerState.setPageIndexWithAnimate(it)
                        },
                        modifier = Modifier.fillMaxWidth().height(35.dp),
                        margin = 28.dp,
                    )
                })
            },
            content = { paddingValues ->
                ComposePager(
                    pageCount = projectSortList.size,
                    modifier = Modifier.fillMaxSize()
                        .padding(top = paddingValues.calculateTopPadding()),
                    composePagerState = pagerState,
                    orientation = Orientation.Horizontal, userEnable = false,
                    content = {
                        val id = projectSortList[pagerState.getCurrSelectIndex()].id
                        ProjectListScreen(navigator, id)
                    }
                )
            })
    }
}