package org.liupack.wanandroid.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemKey
import com.lt.compose_views.image_banner.ImageBanner
import com.seiko.imageloader.rememberImagePainter
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator
import org.liupack.wanandroid.composables.PagingFullLoadLayout
import org.liupack.wanandroid.composables.pagingFooter
import org.liupack.wanandroid.model.UiState.Companion.successDataOrNull
import org.liupack.wanandroid.model.entity.HomeArticleItemData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navigator: Navigator = rememberNavigator()) {
    val viewModel = koinViewModel(HomeViewModel::class)
    LaunchedEffect(Unit) {
        viewModel.dispatch(HomeAction.Refresh)
    }
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            TopAppBar(
                title = {
                    Text("首页")
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.dispatch(HomeAction.OpenGithub)
                    }, content = {
                        Icon(imageVector = Icons.Outlined.Public, contentDescription = null)
                    })
                },
            )
        },
        content = { paddingValues ->
            ArticleContent(
                paddingValues = paddingValues,
                viewModel = viewModel
            )
        },
    )
}

@Composable
private fun ArticleContent(
    paddingValues: PaddingValues,
    viewModel: HomeViewModel = koinViewModel(HomeViewModel::class),
) {
    val articles = viewModel.pagingState.collectAsLazyPagingItems()
    val bannerState by viewModel.bannerList.collectAsStateWithLifecycle()
    val lazyListState = if (articles.itemCount > 0) viewModel.lazyListState else LazyListState()
    PagingFullLoadLayout(pagingState = articles, content = {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues),
            state = lazyListState
        ) {
            item {
                val bannerList = bannerState.successDataOrNull.orEmpty()
                ImageBanner(
                    bannerList.size,
                    imageContent = {
                        Image(
                            painter = rememberImagePainter(bannerList[index].imagePath.orEmpty()),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    indicatorItem = null,
                    selectIndicatorItem = null,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            items(
                count = articles.itemCount,
                key = articles.itemKey { it.id },
                itemContent = { index ->
                    articles[index]?.let { ArticleItem(it) }
                })
            pagingFooter(articles)
        }
    })
}

@Composable
fun ArticleItem(data: HomeArticleItemData) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(buildAnnotatedString {
                if (data.fresh == true) {
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append("新")
                    }
                }
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.tertiary)) {
                    append(data.author ?: data.shareUser ?: "")
                }
            }, style = MaterialTheme.typography.titleSmall)
            Text(
                data.niceDate.orEmpty(),
                color = MaterialTheme.colorScheme.outlineVariant,
                style = MaterialTheme.typography.titleSmall
            )
        }
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(data.title.orEmpty())
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(data.superChapterName.orEmpty(), style = MaterialTheme.typography.labelSmall)
            IconButton(onClick = {}, content = {
                if (data.collect == true) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder, contentDescription = null
                    )
                }
            })
        }
    }
}