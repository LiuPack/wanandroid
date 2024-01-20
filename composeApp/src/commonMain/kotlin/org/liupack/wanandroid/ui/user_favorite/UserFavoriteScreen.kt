package org.liupack.wanandroid.ui.user_favorite

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
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
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemKey
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import org.liupack.wanandroid.common.RouterKey
import org.liupack.wanandroid.common.parametersOf
import org.liupack.wanandroid.composables.IconBackButton
import org.liupack.wanandroid.composables.PagingFullLoadLayout
import org.liupack.wanandroid.composables.pagingFooter
import org.liupack.wanandroid.model.entity.UserFavoriteArticleData
import org.liupack.wanandroid.router.Router

fun RouteBuilder.userFavoriteScreen(navigator: Navigator) {
    scene(Router.UserFavorite.path) {
        UserFavoriteScreen(navigator)
    }
}

@Composable
fun UserFavoriteScreen(navigator: Navigator) {
    val viewModel = koinViewModel(UserFavoriteViewModel::class)
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            UserFavoriteTopBar(navigator)
        },
        content = { paddingValues ->
            val favoriteArticlesState = viewModel.favoriteArticles.collectAsLazyPagingItems()
            PagingFullLoadLayout(
                modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
                pagingState = favoriteArticlesState,
                content = {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            favoriteArticlesState.itemCount,
                            key = favoriteArticlesState.itemKey { it.id }) { index ->
                            val data = favoriteArticlesState[index]
                            if (data != null) {
                                FavoriteArticleItem(
                                    modifier = Modifier.fillMaxWidth(),
                                    data = data,
                                    onClick = {
                                        val path =
                                            Router.WebView.parametersOf(RouterKey.url to it.link)
                                        navigator.navigate(
                                            route = path,
                                            options = NavOptions(launchSingleTop = true)
                                        )
                                    })
                            }
                        }
                        pagingFooter(pagingState = favoriteArticlesState)
                    }
                },
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserFavoriteTopBar(navigator: Navigator) {
    TopAppBar(
        navigationIcon = { IconBackButton(onClick = { navigator.goBack() }) },
        title = { Text("我的收藏") })
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FavoriteArticleItem(
    modifier: Modifier = Modifier,
    data: UserFavoriteArticleData,
    onLongClick: (UserFavoriteArticleData) -> Unit = {},
    onClick: (UserFavoriteArticleData) -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth().clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.large)
            .combinedClickable(
                onLongClick = { onLongClick.invoke(data) },
                onClick = { onClick.invoke(data) })
            .padding(12.dp).clipToBounds(),
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
            text = data.chapterName.orEmpty().ifEmpty { data.author.orEmpty() },
            color = MaterialTheme.colorScheme.primary,
            fontSize = MaterialTheme.typography.titleSmall.fontSize,
            modifier = Modifier.align(Alignment.End)
        )
        Text(
            text = data.niceDate.orEmpty(),
            color = MaterialTheme.colorScheme.outline,
            fontSize = MaterialTheme.typography.titleSmall.fontSize,
            modifier = Modifier.align(Alignment.End),
        )
    }
}