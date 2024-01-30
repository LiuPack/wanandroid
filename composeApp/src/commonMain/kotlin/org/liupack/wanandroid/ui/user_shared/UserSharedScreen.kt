package org.liupack.wanandroid.ui.user_shared

import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.LoadStateLoading
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemKey
import com.lt.compose_views.refresh_layout.PullToRefresh
import com.lt.compose_views.refresh_layout.RefreshContentStateEnum
import com.lt.compose_views.refresh_layout.rememberRefreshLayoutState
import kotlinx.coroutines.launch
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.liupack.wanandroid.composables.ArticleItem
import org.liupack.wanandroid.composables.CustomPullToRefreshContent
import org.liupack.wanandroid.composables.DeletedDialog
import org.liupack.wanandroid.composables.IconBackButton
import org.liupack.wanandroid.composables.LoadingDialog
import org.liupack.wanandroid.composables.MessageDialog
import org.liupack.wanandroid.composables.PagingFullLoadLayout
import org.liupack.wanandroid.model.UiStateException
import org.liupack.wanandroid.model.UiStateFailed
import org.liupack.wanandroid.model.UiStateLoading
import org.liupack.wanandroid.model.UiStateSuccess
import org.liupack.wanandroid.router.Router

fun RouteBuilder.userSharedScreen(navigator: Navigator) {
    scene(
        route = Router.UserShared.path, navTransition = NavTransition(
            createTransition = expandHorizontally(),
            destroyTransition = shrinkHorizontally(),
        )
    ) {
        UserSharedScreen(navigator)
    }
}

@Composable
fun UserSharedScreen(navigator: Navigator) {
    val viewModel = koinViewModel(UserSharedViewModel::class)
    val shareArticlesState = viewModel.shareArticles.collectAsLazyPagingItems()
    val refreshLayoutState = rememberRefreshLayoutState { shareArticlesState.refresh() }
    LaunchedEffect(shareArticlesState.loadState.refresh) {
        val refreshState =
            if (shareArticlesState.loadState.refresh is LoadStateLoading) RefreshContentStateEnum.Refreshing else RefreshContentStateEnum.Stop
        refreshLayoutState.setRefreshState(refreshState)
    }
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        UserSharedTopAppBar(
            navigator = navigator,
            onNavigateForResult = {
                if (it) {
                    shareArticlesState.refresh()
                }
            }
        )
    }, content = { paddingValues ->
        PullToRefresh(
            refreshLayoutState = refreshLayoutState,
            modifier = Modifier.fillMaxSize().padding(top = paddingValues.calculateTopPadding()),
            refreshContent = remember { { CustomPullToRefreshContent() } },
        ) {
            PagingFullLoadLayout(
                modifier = Modifier.fillMaxSize(),
                pagingState = shareArticlesState,
                content = {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().align(Alignment.Center),
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) {
                        items(
                            shareArticlesState.itemCount,
                            key = shareArticlesState.itemKey { it.id }) { index ->
                            val item = shareArticlesState[index]
                            if (item != null) {
                                val deletedVisibility by viewModel.deletedVisibility.collectAsState(
                                    false
                                )
                                val deletedState by viewModel.deletedState.collectAsState(null)
                                when (val state = deletedState) {
                                    is UiStateLoading -> {
                                        LoadingDialog(true)
                                    }

                                    is UiStateException -> {
                                        MessageDialog(true, state.throwable.stackTraceToString())
                                    }

                                    is UiStateFailed -> {
                                        MessageDialog(true, state.message)
                                    }

                                    is UiStateSuccess -> {
                                        shareArticlesState.refresh()
                                    }

                                    else -> {}
                                }
                                DeletedDialog(
                                    isVisibility = deletedVisibility,
                                    message = "是否删除当前分享的文章",
                                    onDeleted = {
                                        viewModel.dispatch(UserSharedAction.ShowDeleted(false))
                                        viewModel.dispatch(UserSharedAction.Deleted(item.id))
                                    },
                                    onDismiss = {
                                        viewModel.dispatch(UserSharedAction.ShowDeleted(false))
                                    })
                                ArticleItem(data = item, favoriteVisibility = false, onLongClick = {
                                    viewModel.dispatch(UserSharedAction.ShowDeleted(true))
                                })
                            }
                        }
                    }
                },
            )
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserSharedTopAppBar(navigator: Navigator, onNavigateForResult: (Boolean) -> Unit = {}) {
    val scope = rememberCoroutineScope()
    TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.background,
        navigationIconContentColor = contentColorFor(MaterialTheme.colorScheme.background),
        titleContentColor = contentColorFor(MaterialTheme.colorScheme.background),
        actionIconContentColor = contentColorFor(MaterialTheme.colorScheme.background),
    ), title = { Text(text = "我的分享") }, actions = {
        IconButton(onClick = {
            scope.launch {
                val result = navigator.navigateForResult(
                    route = Router.UserAddShared.path,
                    options = NavOptions(launchSingleTop = true)
                )
                onNavigateForResult.invoke(result == true)
            }
        }, content = {
            Icon(
                imageVector = Icons.Outlined.Add, contentDescription = null
            )
        })
    }, navigationIcon = { IconBackButton(onClick = { navigator.goBack() }) })
}
