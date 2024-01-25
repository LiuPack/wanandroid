package org.liupack.wanandroid.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Public
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.LoadStateLoading
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemKey
import com.lt.compose_views.refresh_layout.PullToRefresh
import com.lt.compose_views.refresh_layout.RefreshContentStateEnum
import com.lt.compose_views.refresh_layout.RefreshLayoutState
import com.lt.compose_views.refresh_layout.rememberRefreshLayoutState
import kotlinx.coroutines.launch
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.BackHandler
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.liupack.wanandroid.common.Constants
import org.liupack.wanandroid.common.RouterKey
import org.liupack.wanandroid.common.parametersOf
import org.liupack.wanandroid.composables.ArticleItem
import org.liupack.wanandroid.composables.CustomPullToRefreshContent
import org.liupack.wanandroid.composables.PagingFullLoadLayout
import org.liupack.wanandroid.composables.pagingFooter
import org.liupack.wanandroid.model.UiState.Companion.isLoginExpired
import org.liupack.wanandroid.model.UiStateSuccess
import org.liupack.wanandroid.model.entity.HomeArticleItemData
import org.liupack.wanandroid.platform.exitApp
import org.liupack.wanandroid.router.Router
import org.liupack.wanandroid.theme.LocalShowPinned

fun RouteBuilder.homeScreen(navigator: Navigator) {
    scene(route = Router.Home.path, navTransition = NavTransition()) {
        BackHandler { exitApp() }
        val viewModel = koinViewModel(HomeViewModel::class)
        val articleState = viewModel.articles.collectAsLazyPagingItems()
        val pinnedState by viewModel.pinned.collectAsState(emptyList())
        val lazyListState = rememberLazyListState()
        val favoriteState by viewModel.favoriteState.collectAsState(null)
        var isFavoriteAction by remember { mutableStateOf(false) }
        val refreshLayoutState = rememberRefreshLayoutState {
            isFavoriteAction = false
            viewModel.dispatch(HomeAction.Refresh)
            articleState.refresh()
        }
        favoriteState?.let {
            LaunchedEffect(it) {
                if (it.isLoginExpired) {
                    val result = navigator.navigateForResult(
                        Router.Login.path, NavOptions(launchSingleTop = true)
                    )
                    if (result == true) {
                        viewModel.dispatch(HomeAction.Refresh)
                        articleState.refresh()
                    }
                }
                if (favoriteState is UiStateSuccess) {
                    isFavoriteAction = true
                    viewModel.dispatch(HomeAction.Refresh)
                    articleState.refresh()
                }
            }
        }
        LaunchedEffect(articleState.loadState.refresh) {
            val canRefresh = articleState.loadState.refresh is LoadStateLoading && !isFavoriteAction
            refreshLayoutState.setRefreshState(if (canRefresh) RefreshContentStateEnum.Refreshing else RefreshContentStateEnum.Stop)
        }
        HomeScreen(
            navigator = navigator,
            refreshLayoutState = refreshLayoutState,
            pinnedState = pinnedState,
            lazyPagingItems = articleState,
            lazyListState = lazyListState,
            addFavorite = {
                viewModel.dispatch(HomeAction.Favorite(it))
            },
            cancelFavorite = {
                viewModel.dispatch(HomeAction.CancelFavorite(it))
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    navigator: Navigator,
    refreshLayoutState: RefreshLayoutState,
    pinnedState: List<HomeArticleItemData>,
    lazyPagingItems: LazyPagingItems<HomeArticleItemData>,
    lazyListState: LazyListState,
    addFavorite: (HomeArticleItemData) -> Unit = {},
    cancelFavorite: (HomeArticleItemData) -> Unit = {},
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("首页") },
                actions = {
                    IconButton(
                        onClick = {
                            val path =
                                Router.WebView.parametersOf(RouterKey.url to Constants.projectUrl)
                            navigator.navigate(path, NavOptions(launchSingleTop = true))
                        },
                        content = {
                            Icon(Icons.Outlined.Public, null)
                        },
                    )
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = contentColorFor(MaterialTheme.colorScheme.background),
                    actionIconContentColor = contentColorFor(MaterialTheme.colorScheme.background),
                    navigationIconContentColor = contentColorFor(MaterialTheme.colorScheme.background),
                )
            )
        },
        content = { paddingValues ->
            PullToRefresh(
                modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
                refreshLayoutState = refreshLayoutState,
                refreshContent = remember { { CustomPullToRefreshContent() } },
                content = {
                    val showPinned by LocalShowPinned.current
                    val scope = rememberCoroutineScope()
                    PagingFullLoadLayout(
                        modifier = Modifier.fillMaxSize(),
                        pagingState = lazyPagingItems
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(12.dp),
                            state = lazyListState
                        ) {
                            if (showPinned) {
                                if (lazyListState.firstVisibleItemIndex == 0) {
                                    scope.launch {
                                        lazyListState.animateScrollToItem(0)
                                    }
                                }
                                items(pinnedState, key = { it.id }) { item ->
                                    ArticleItem(data = item, onClick = {
                                        val path =
                                            Router.WebView.parametersOf(RouterKey.url to it.link)
                                        navigator.navigate(
                                            route = path,
                                            options = NavOptions(launchSingleTop = true)
                                        )
                                    }, onFavoriteClick = { favoriteState ->
                                        if (favoriteState) {
                                            addFavorite.invoke(item)
                                        } else {
                                            cancelFavorite.invoke(item)
                                        }
                                    })
                                }
                            }
                            items(count = lazyPagingItems.itemCount,
                                key = lazyPagingItems.itemKey { it.id },
                                itemContent = { index ->
                                    val item = lazyPagingItems[index]
                                    if (item != null) {
                                        ArticleItem(data = item, onClick = {
                                            val path =
                                                Router.WebView.parametersOf(RouterKey.url to it.link)
                                            navigator.navigate(
                                                route = path,
                                                options = NavOptions(launchSingleTop = true)
                                            )
                                        }, onFavoriteClick = { favoriteState ->
                                            if (favoriteState) {
                                                addFavorite.invoke(item)
                                            } else {
                                                cancelFavorite.invoke(item)
                                            }
                                        })
                                    }
                                })
                            pagingFooter(lazyPagingItems)
                        }
                    }
                })
        })
}