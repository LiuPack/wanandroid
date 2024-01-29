package org.liupack.wanandroid.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.BackHandler
import moe.tlaster.precompose.navigation.BackStackEntry
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
import org.liupack.wanandroid.openUrl
import org.liupack.wanandroid.platform.exitApp
import org.liupack.wanandroid.platform.settings
import org.liupack.wanandroid.router.Router
import org.liupack.wanandroid.theme.LocalShowPinned

fun RouteBuilder.homeScreen(navigator: Navigator) {
    scene(route = Router.Home.path, navTransition = NavTransition()) {
        HomeScreen(navigator = navigator, backStackEntry = it)
    }
}

@Composable
fun HomeScreen(navigator: Navigator, backStackEntry: BackStackEntry) {
    BackHandler { exitApp() }
    val viewModel = koinViewModel(HomeViewModel::class)
    val combine by viewModel.combineData.collectAsState()
    val pinned = combine.first
    val articleState = combine.second.collectAsLazyPagingItems()
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
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = { HomeTopAppBar() },
        content = { paddingValues ->
            HomeContent(
                paddingValues = paddingValues,
                refreshLayoutState = refreshLayoutState,
                articleState = articleState,
                pinned = pinned,
                lazyListState = lazyListState,
                navigator = navigator,
                viewModel = viewModel
            )
        })
}

@Composable
private fun HomeContent(
    paddingValues: PaddingValues,
    refreshLayoutState: RefreshLayoutState,
    articleState: LazyPagingItems<HomeArticleItemData>,
    pinned: List<HomeArticleItemData>,
    lazyListState: LazyListState,
    navigator: Navigator,
    viewModel: HomeViewModel
) {
    val showPinned by LocalShowPinned.current
    PullToRefresh(modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
        refreshLayoutState = refreshLayoutState,
        refreshContent = remember { { CustomPullToRefreshContent() } },
        content = {
            PagingFullLoadLayout(
                modifier = Modifier.fillMaxSize(), pagingState = articleState
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(12.dp),
                    state = lazyListState
                ) {
                    if (showPinned) {
                        homePinnedArticleItem(
                            articleState = pinned,
                            navigator = navigator,
                            viewModel = viewModel
                        )
                    }
                    homeDefaultArticleItem(
                        articleState = articleState,
                        navigator = navigator,
                        viewModel = viewModel
                    )
                    pagingFooter(pagingState = articleState)
                }
            }
        })
}

private fun LazyListScope.homePinnedArticleItem(
    articleState: List<HomeArticleItemData>,
    navigator: Navigator,
    viewModel: HomeViewModel
) {
    items(articleState,
        key = { it.id },
        itemContent = { item ->
            ArticleItem(data = item, showPinned = true, onClick = {
                val path = Router.WebView.parametersOf(RouterKey.url to it.link)
                navigator.navigate(
                    route = path, options = NavOptions(launchSingleTop = true)
                )
            }, onFavoriteClick = { favoriteState ->
                if (favoriteState) {
                    viewModel.dispatch(HomeAction.Favorite(item))
                } else {
                    viewModel.dispatch(HomeAction.CancelFavorite(item))
                }
            })
        })
}

private fun LazyListScope.homeDefaultArticleItem(
    articleState: LazyPagingItems<HomeArticleItemData>,
    navigator: Navigator,
    viewModel: HomeViewModel
) {
    items(count = articleState.itemCount,
        key = articleState.itemKey { it.id },
        itemContent = { index ->
            val item = articleState[index]
            if (item != null) {
                ArticleItem(data = item, showPinned = false, onClick = {
                    val path = Router.WebView.parametersOf(RouterKey.url to it.link)
                    navigator.navigate(
                        route = path, options = NavOptions(launchSingleTop = true)
                    )
                }, onFavoriteClick = { favoriteState ->
                    if (favoriteState) {
                        viewModel.dispatch(HomeAction.Favorite(item))
                    } else {
                        viewModel.dispatch(HomeAction.CancelFavorite(item))
                    }
                })
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopAppBar() {
    var showPinned by LocalShowPinned.current
    var expanned by remember { mutableStateOf(false) }
    TopAppBar(title = { Text("首页") }, actions = {
        IconButton(onClick = {
            expanned = true
        }, content = {
            Icon(imageVector = Icons.Outlined.MoreVert, contentDescription = null)
        })
        DropdownMenu(expanded = expanned, onDismissRequest = { expanned = false }) {
            DropdownMenuItem(text = { Text("打开项目主页") }, onClick = {
                openUrl(Constants.projectUrl)
                expanned = false
            })
            DropdownMenuItem(text = {
                Text(if (showPinned) "隐藏置顶文章" else "显示置顶文章")
            }, onClick = {
                showPinned = !showPinned
                settings.putBoolean(Constants.showPinned, showPinned)
                expanned = false
            })
        }
    }, colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.background,
        titleContentColor = contentColorFor(MaterialTheme.colorScheme.background),
        actionIconContentColor = contentColorFor(MaterialTheme.colorScheme.background),
        navigationIconContentColor = contentColorFor(MaterialTheme.colorScheme.background),
    )
    )
}