package org.liupack.wanandroid.ui.system

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.liupack.wanandroid.composables.FullUiStateLayout
import org.liupack.wanandroid.composables.rememberLogger
import org.liupack.wanandroid.model.UiState
import org.liupack.wanandroid.model.entity.SystemBaseData
import org.liupack.wanandroid.router.Router

typealias OnSystemItemClick = (title: String, defaultIndex: Int, children: List<SystemBaseData>) -> Unit

fun RouteBuilder.systemScreen(navigator: Navigator) {
    scene(route = Router.System.path,navTransition = NavTransition()) {
        val logger = rememberLogger()
        val viewModel = koinViewModel(SystemViewModel::class)
        logger.i("初始化：systemScreen:${viewModel.hashCode()}")
        LaunchedEffect(viewModel.hashCode()) {
            viewModel.dispatch(SystemAction.Init)
        }
        val uiState by viewModel.systemBaseData.collectAsState()
        val lazyListState = rememberLazyListState()
        SystemScreen(
            navigator = navigator,
            lazyListState = lazyListState,
            uiState = uiState,
            onRetry = { viewModel.dispatch(SystemAction.Refresh) })
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun SystemScreen(
    navigator: Navigator,
    lazyListState: LazyListState,
    uiState: UiState<List<SystemBaseData>>,
    onRetry: () -> Unit = {},
) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = { Text("知识体系") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
            )
        )
    }, content = { paddingValues ->
        FullUiStateLayout(
            modifier = Modifier.fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
            uiState = uiState,
            onRetry = onRetry,
            content = { dataList ->
                LazyColumn(modifier = Modifier.fillMaxSize(),
                    state = lazyListState,
                    content = {
                        dataList.forEach { systemBaseData ->
                            stickyHeader(key = systemBaseData.id) {
                                Box(
                                    modifier = Modifier.fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.surface)
                                        .padding(12.dp)
                                ) {
                                    Text(systemBaseData.name)
                                }
                            }
                            items(systemBaseData.children.orEmpty(), key = { it.id }) { data ->
                                Box(
                                    modifier = Modifier.fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.surface)
                                        .padding(12.dp)
                                ) {
                                    Text(data.name)
                                }
                            }
                        }
                    })
            })
    })
}
