package org.liupack.wanandroid.ui.wechat_account.child

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
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
import com.lt.compose_views.refresh_layout.rememberRefreshLayoutState
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.BackHandler
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import org.koin.core.parameter.parametersOf
import org.liupack.wanandroid.common.RouterKey
import org.liupack.wanandroid.common.parametersOf
import org.liupack.wanandroid.composables.ArticleItem
import org.liupack.wanandroid.composables.PagingFullLoadLayout
import org.liupack.wanandroid.composables.pagingFooter
import org.liupack.wanandroid.composables.rememberLazyListState
import org.liupack.wanandroid.model.UiState.Companion.isLoginExpired
import org.liupack.wanandroid.model.UiStateSuccess
import org.liupack.wanandroid.model.entity.HomeArticleItemData
import org.liupack.wanandroid.platform.exitApp
import org.liupack.wanandroid.router.Router

fun RouteBuilder.articleInWechatAccount(parentNavigator: Navigator, router: String, id: Int) {
    scene(router) {
        BackHandler { exitApp() }
        val viewModel = koinViewModel(
            vmClass = ArticleInWechatAccountViewModel::class,
            key = id.toString()
        ) { parametersOf(id) }
        val articleState = viewModel.article.collectAsLazyPagingItems()
        val lazyListState = articleState.rememberLazyListState()
        val favoriteState by viewModel.favoriteState.collectAsState(null)
        var isFavoriteAction by remember { mutableStateOf(false) }
        favoriteState?.let {
            LaunchedEffect(it) {
                if (it.isLoginExpired) {
                    val result = parentNavigator.navigateForResult(
                        Router.Login.path,
                        NavOptions(launchSingleTop = true)
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
        val refreshLayoutState = rememberRefreshLayoutState {
            isFavoriteAction = false
            articleState.refresh()
        }
        LaunchedEffect(articleState.loadState.refresh) {
            val canRefresh = articleState.loadState.refresh is LoadStateLoading && !isFavoriteAction
            refreshLayoutState.setRefreshState(if (canRefresh) RefreshContentStateEnum.Refreshing else RefreshContentStateEnum.Stop)
        }
        PullToRefresh(
            refreshLayoutState = refreshLayoutState,
            modifier = Modifier.fillMaxSize(),
            content = {
                ArticleInWechatAccountScreen(
                    navigator = parentNavigator,
                    lazyPagingItems = articleState,
                    lazyListState = lazyListState,
                    addFavorite = {
                        viewModel.dispatch(ArticleInWechatAccountAction.Favorite(it.id))
                    },
                    cancelFavorite = {
                        viewModel.dispatch(ArticleInWechatAccountAction.CancelFavorite(it.id))
                    },
                )
            },
        )
    }
}

@Composable
fun ArticleInWechatAccountScreen(
    navigator: Navigator,
    lazyPagingItems: LazyPagingItems<HomeArticleItemData>,
    lazyListState: LazyListState,
    addFavorite: (HomeArticleItemData) -> Unit = {},
    cancelFavorite: (HomeArticleItemData) -> Unit = {},
) {
    PagingFullLoadLayout(
        modifier = Modifier.fillMaxSize(),
        pagingState = lazyPagingItems,
        content = {
            LazyColumn(
                modifier = Modifier,
                state = lazyListState,
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(lazyPagingItems.itemCount, key = lazyPagingItems.itemKey { it.id }) {
                    val data = lazyPagingItems[it]
                    if (data != null) {
                        ArticleItem(data = data, onClick = {
                            val path = Router.WebView.parametersOf(RouterKey.url to data.link)
                            navigator.navigate(
                                route = path,
                                options = NavOptions(launchSingleTop = true)
                            )
                        }, onFavoriteClick = { favoriteState ->
                            if (favoriteState) {
                                addFavorite.invoke(data)
                            } else {
                                cancelFavorite.invoke(data)
                            }
                        })
                    }
                }
                pagingFooter(lazyPagingItems)
            }
        })
}