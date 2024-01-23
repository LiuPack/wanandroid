package org.liupack.wanandroid.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemKey
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
import org.liupack.wanandroid.composables.PagingFullLoadLayout
import org.liupack.wanandroid.composables.pagingFooter
import org.liupack.wanandroid.model.entity.HomeArticleItemData
import org.liupack.wanandroid.platform.exitApp
import org.liupack.wanandroid.router.Router

fun RouteBuilder.homeScreen(navigator: Navigator) {
    scene(route = Router.Home.path, navTransition = NavTransition()) {
        BackHandler { exitApp() }
        val viewModel = koinViewModel(HomeViewModel::class)
        val articleState = viewModel.articles.collectAsLazyPagingItems()
        val lazyListState = rememberLazyListState()
        val favoriteState by viewModel.favoriteState.collectAsState(null)
        LaunchedEffect(favoriteState) {
            if (favoriteState == true) {
                articleState.refresh()
            }
        }
        HomeScreen(
            navigator = navigator,
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
            PagingFullLoadLayout(
                modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
                pagingState = lazyPagingItems
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(12.dp),
                    state = lazyListState
                ) {
                    items(count = lazyPagingItems.itemCount,
                        key = lazyPagingItems.itemKey { it.id },
                        itemContent = { index ->
                            val item = lazyPagingItems[index]
                            if (item != null) {
                                ArticleItem(data = item, onClick = {
                                    val path = Router.WebView.parametersOf(RouterKey.url to it.link)
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
}