package org.liupack.wanandroid.ui.system

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.BackHandler
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.liupack.wanandroid.common.RouterKey
import org.liupack.wanandroid.common.parametersOf
import org.liupack.wanandroid.composables.FullUiStateLayout
import org.liupack.wanandroid.composables.rememberLogger
import org.liupack.wanandroid.model.UiState
import org.liupack.wanandroid.model.entity.SystemBaseData
import org.liupack.wanandroid.platform.exitApp
import org.liupack.wanandroid.router.Router

fun RouteBuilder.systemScreen(navigator: Navigator) {
    scene(route = Router.System.path, navTransition = NavTransition()) {
        BackHandler { exitApp() }
        val logger = rememberLogger()
        val viewModel = koinViewModel(SystemViewModel::class)
        logger.i("初始化：systemScreen:${viewModel.hashCode()}")
        LaunchedEffect(viewModel.hashCode()) {
            viewModel.dispatch(SystemAction.Init)
        }
        val uiState by viewModel.systemBaseData.collectAsState()
        val lazyListState = rememberLazyListState()
        SystemScreen(navigator = navigator,
            lazyListState = lazyListState,
            uiState = uiState,
            onRetry = { viewModel.dispatch(SystemAction.Refresh) })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SystemScreen(
    navigator: Navigator,
    lazyListState: LazyListState,
    uiState: UiState<List<SystemBaseData>>,
    onRetry: () -> Unit = {},
) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = { Text("知识体系") }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = contentColorFor(MaterialTheme.colorScheme.background),
                actionIconContentColor = contentColorFor(MaterialTheme.colorScheme.background),
                navigationIconContentColor = contentColorFor(MaterialTheme.colorScheme.background),
            )
        )
    }, content = { paddingValues ->
        FullUiStateLayout(modifier = Modifier.fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding()),
            uiState = uiState,
            onRetry = onRetry,
            content = { dataList ->
                LazyColumn(modifier = Modifier.fillMaxSize(),
                    state = lazyListState,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    content = {
                        dataList.forEach { systemBaseData ->
                            systemParent(systemBaseData)
                            systemChild(systemBaseData, navigator)
                        }
                    })
            })
    })
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.systemParent(systemBaseData: SystemBaseData) {
    stickyHeader(key = systemBaseData.id) {
        Box(
            modifier = Modifier.fillMaxWidth().shadow(2.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(12.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = systemBaseData.name, style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

private fun LazyListScope.systemChild(systemBaseData: SystemBaseData, navigator: Navigator) {
    items(systemBaseData.children.orEmpty(), key = { it.id }) { data ->
        Box(
            modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface)
                .clickable {
                    val path = Router.ArticleInSystem.parametersOf(
                        RouterKey.title to data.name,
                        RouterKey.id to data.id,
                    )
                    navigator.navigate(path, options = NavOptions(launchSingleTop = true))
                }.padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(data.name)
        }
    }
}
