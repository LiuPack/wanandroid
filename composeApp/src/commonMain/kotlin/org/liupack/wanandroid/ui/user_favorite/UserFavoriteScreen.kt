package org.liupack.wanandroid.ui.user_favorite

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
            val favoriteState by viewModel.favoriteState.collectAsState(null)
            if (favoriteState != null) {
                favoriteArticlesState.refresh()
            }
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
                                    }, onFavoriteClick = {
                                        viewModel.dispatch(
                                            UserFavoriteAction.CancelFavorite(
                                                id = id,
                                                originId = originId
                                            )
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
    onFavoriteClick: UserFavoriteArticleData.(Boolean) -> Unit = {},
    onLongClick: (UserFavoriteArticleData) -> Unit = {},
    onClick: (UserFavoriteArticleData) -> Unit = {}
) {
    val favoriteState by rememberSaveable(data.id) { mutableStateOf(true) }
    Column(
        modifier = modifier.fillMaxWidth().clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.large)
            .combinedClickable(onLongClick = { onLongClick.invoke(data) },
                onClick = { onClick.invoke(data) }).padding(12.dp).clipToBounds(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = data.title,
            maxLines = 2,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = data.author.orEmpty(),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    modifier = Modifier.align(Alignment.Start)
                )
                Text(
                    text = data.niceDate.orEmpty(),
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    modifier = Modifier.align(Alignment.Start),
                )
            }
            IconToggleButton(
                checked = favoriteState,
                onCheckedChange = {
                    onFavoriteClick.invoke(data, it)
                },
                content = {
                    Icon(
                        imageVector = if (favoriteState) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                },
            )
        }

    }
}