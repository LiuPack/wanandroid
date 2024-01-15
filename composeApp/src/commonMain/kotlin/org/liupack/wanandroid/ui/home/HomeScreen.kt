package org.liupack.wanandroid.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
import org.liupack.wanandroid.composables.PagingFullLoadLayout
import org.liupack.wanandroid.composables.pagingFooter
import org.liupack.wanandroid.model.entity.HomeArticleItemData
import org.liupack.wanandroid.openUrl
import org.liupack.wanandroid.platform.exitApp
import org.liupack.wanandroid.router.Router

fun RouteBuilder.homeScreen(navigator: Navigator) {
    scene(route = Router.Home.path, navTransition = NavTransition()) {
        BackHandler { exitApp() }
        val viewModel = koinViewModel(HomeViewModel::class)
        val articleState = viewModel.articles.collectAsLazyPagingItems()
        val lazyListState = rememberLazyListState()
        HomeScreen(navigator, articleState, lazyListState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    navigator: Navigator,
    lazyPagingItems: LazyPagingItems<HomeArticleItemData>,
    lazyListState: LazyListState,
) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = { Text("首页") },
            actions = {
                IconButton(
                    onClick = {
                        openUrl(Constants.projectUrl)
                    },
                    content = {
                        Icon(Icons.Outlined.Public, null)
                    },
                )
            },
        )
    }, content = { paddingValues ->
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
                            })
                        }
                    })
                pagingFooter(lazyPagingItems)
            }
        }
    })
}

@Composable
fun ArticleItem(data: HomeArticleItemData, onClick: (HomeArticleItemData) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.large)
            .clickable { onClick.invoke(data) }.padding(12.dp).clipToBounds(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = data.title,
            maxLines = 2,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = data.shareUser.orEmpty().ifEmpty { data.author.orEmpty() },
            color = MaterialTheme.colorScheme.primary,
            fontSize = MaterialTheme.typography.titleSmall.fontSize,
            modifier = Modifier.align(Alignment.End)
        )
        Text(
            text = data.niceDate.orEmpty().ifEmpty { data.niceShareDate.orEmpty() },
            color = MaterialTheme.colorScheme.outline,
            fontSize = MaterialTheme.typography.titleSmall.fontSize,
            modifier = Modifier.align(Alignment.End),
        )
    }
}

