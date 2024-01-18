package org.liupack.wanandroid.ui.wechat_account.child

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.LoadStateLoading
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemKey
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.BackHandler
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import org.koin.core.parameter.parametersOf
import org.liupack.wanandroid.common.RouterKey
import org.liupack.wanandroid.common.collectAsLazyEmptyPagingItems
import org.liupack.wanandroid.common.parametersOf
import org.liupack.wanandroid.composables.ArticleItem
import org.liupack.wanandroid.composables.PagingFullLoadLayout
import org.liupack.wanandroid.composables.pagingFooter
import org.liupack.wanandroid.composables.rememberLazyListState
import org.liupack.wanandroid.model.entity.HomeArticleItemData
import org.liupack.wanandroid.platform.exitApp
import org.liupack.wanandroid.router.Router

fun RouteBuilder.articleInWechatAccount(parentNavigator: Navigator, router: String, id: Int) {
    scene(router) {
        BackHandler { exitApp() }
        ArticleInWechatAccountScreen(parentNavigator, id)
    }
}

@Composable
fun ArticleInWechatAccountScreen(navigator: Navigator, id: Int) {
    val viewModel = koinViewModel(ArticleInWechatAccountViewModel::class) { parametersOf(id) }
    val articleState = viewModel.article.collectAsLazyPagingItems()
    val lazyListState = articleState.rememberLazyListState()
    PagingFullLoadLayout(modifier = Modifier.fillMaxSize(), pagingState = articleState, content = {
        ArticleList(
            modifier = Modifier.fillMaxSize(),
            navigator = navigator,
            articleList = articleState,
            lazyListState = lazyListState
        )
    })
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BoxScope.ArticleList(
    modifier: Modifier,
    navigator: Navigator,
    articleList: LazyPagingItems<HomeArticleItemData> = collectAsLazyEmptyPagingItems(),
    lazyListState: LazyListState,
) {
    val refreshState = rememberPullRefreshState(
        refreshing = articleList.loadState.refresh is LoadStateLoading,
        onRefresh = {
            articleList.refresh()
        })
    LazyColumn(
        modifier = modifier.pullRefresh(refreshState),
        state = lazyListState,
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(articleList.itemCount, key = articleList.itemKey { it.id }) {
            val data = articleList[it]
            if (data != null) {
                ArticleItem(data = data, onClick = {
                    val path = Router.WebView.parametersOf(RouterKey.url to it.link)
                    navigator.navigate(route = path, options = NavOptions(launchSingleTop = true))
                })
            }
        }
        pagingFooter(articleList)
    }
    PullRefreshIndicator(
        state = refreshState,
        refreshing = articleList.loadState.refresh is LoadStateLoading,
        modifier = Modifier.align(Alignment.TopCenter)
    )
}