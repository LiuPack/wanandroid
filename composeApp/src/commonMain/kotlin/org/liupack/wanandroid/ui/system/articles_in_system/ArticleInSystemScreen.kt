package org.liupack.wanandroid.ui.system.articles_in_system

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.LoadStateLoading
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemKey
import com.lt.compose_views.refresh_layout.PullToRefresh
import com.lt.compose_views.refresh_layout.RefreshContentStateEnum
import com.lt.compose_views.refresh_layout.rememberRefreshLayoutState
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.query
import org.koin.core.parameter.parametersOf
import org.liupack.wanandroid.common.RouterKey
import org.liupack.wanandroid.common.parametersOf
import org.liupack.wanandroid.composables.ArticleItem
import org.liupack.wanandroid.composables.CustomPullToRefreshContent
import org.liupack.wanandroid.composables.IconBackButton
import org.liupack.wanandroid.composables.PagingFullLoadLayout
import org.liupack.wanandroid.composables.pagingFooter
import org.liupack.wanandroid.model.UiState.Companion.isLoginExpired
import org.liupack.wanandroid.model.UiStateSuccess
import org.liupack.wanandroid.router.Router

fun RouteBuilder.articleInSystemScreen(navigator: Navigator) {
    scene(Router.ArticleInSystem.path) {
        val title = it.query<String>(RouterKey.title)
        val id = it.query<Int>(RouterKey.id)
        ArticleInSystemScreen(navigator = navigator, title = title, id = id)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArticleInSystemScreen(navigator: Navigator, id: Int?, title: String?) {
    val viewModel =
        koinViewModel(ArticleInSystemViewModel::class, key = id.toString()) { parametersOf(id) }
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = { Text(title ?: "文章") },
            navigationIcon = { IconBackButton(onClick = { navigator.goBack() }) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = contentColorFor(MaterialTheme.colorScheme.background),
                actionIconContentColor = contentColorFor(MaterialTheme.colorScheme.background),
                navigationIconContentColor = contentColorFor(MaterialTheme.colorScheme.background),
            )
        )
    }, content = { paddingValues ->
        val articleState = viewModel.articleState.collectAsLazyPagingItems()
        val favoriteState by viewModel.favoriteState.collectAsState(null)
        var isFavoriteAction by remember { mutableStateOf(false) }
        val refreshLayoutState = rememberRefreshLayoutState {
            isFavoriteAction = false
            articleState.refresh()
        }
        favoriteState?.let {
            LaunchedEffect(it) {
                if (it.isLoginExpired) {
                    val result = navigator.navigateForResult(
                        Router.Login.path, NavOptions(launchSingleTop = true)
                    )
                    if (result == true) {
                        articleState.refresh()
                    }
                }
                if (favoriteState is UiStateSuccess) {
                    isFavoriteAction = true
                    articleState.refresh()
                }
            }
        }
        LaunchedEffect(articleState.loadState.refresh) {
            val canRefresh = articleState.loadState.refresh is LoadStateLoading && !isFavoriteAction
            refreshLayoutState.setRefreshState(if (canRefresh) RefreshContentStateEnum.Refreshing else RefreshContentStateEnum.Stop)
        }
        PullToRefresh(
            refreshLayoutState = refreshLayoutState,
            modifier = Modifier.fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
            refreshContent = remember { { CustomPullToRefreshContent() } },
            content = {
                PagingFullLoadLayout(modifier = Modifier.fillMaxSize(),
                    pagingState = articleState,
                    content = {
                        LazyColumn(modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(12.dp),
                            content = {
                                items(articleState.itemCount,
                                    key = articleState.itemKey { it.id },
                                    itemContent = { index ->
                                        val data = articleState[index]
                                        if (data != null) {
                                            ArticleItem(
                                                data = data,
                                                onClick = {
                                                    val path =
                                                        Router.WebView.parametersOf(RouterKey.url to it.link)
                                                    navigator.navigate(path)
                                                },
                                                onFavoriteClick = {
                                                    if (it) {
                                                        viewModel.dispatch(
                                                            ArticleInSystemAction.Favorite(
                                                                data.id
                                                            )
                                                        )
                                                    } else {
                                                        viewModel.dispatch(
                                                            ArticleInSystemAction.CancelFavorite(
                                                                data.id
                                                            )
                                                        )
                                                    }
                                                },
                                            )
                                        }
                                    })
                                pagingFooter(articleState)
                            })
                    })
            },
        )
    })
}