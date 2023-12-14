package org.liupack.wanandroid.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemContentType
import app.cash.paging.compose.itemKey
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import org.liupack.wanandroid.model.entity.HomeArticleItemData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navigator: Navigator) {
    val viewModel = koinViewModel(HomeViewModel::class)
    LaunchedEffect(viewModel) {
        viewModel.dispatch(HomeAction.Refresh)
    }
    Scaffold(modifier = Modifier.fillMaxWidth(), topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text("首页")
            },
            navigationIcon = {
                IconButton(onClick = {
                    viewModel.dispatch(HomeAction.OpenGithub)
                }, content = {
                    Icon(imageVector = Icons.Outlined.Public, contentDescription = null)
                })
            }
        )
    }) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            ArticleContent(viewModel = viewModel)
        }
    }
}

@Composable
private fun ArticleContent(viewModel: HomeViewModel) {
    val articles = viewModel.pagingState.collectAsLazyPagingItems()
    val lazyListState = if (articles.itemCount > 0) viewModel.lazyListState else LazyListState()
    when (articles.loadState.refresh) {
        is LoadState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Button(onClick = {
                    articles.refresh()
                }, content = {
                    Text("重新加载")
                })
            }
        }

        is LoadState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is LoadState.NotLoading -> {
            if (articles.itemCount == 0) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text("没有数据")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = lazyListState
                ) {
                    items(count = articles.itemCount,
                        key = articles.itemKey { it.id ?: 0 },
                        contentType = articles.itemContentType { "goods" },
                        itemContent = { index ->
                            articles[index]?.let { ArticleItem(data = it) }
                        })
                    when (articles.loadState.append) {
                        is LoadState.Error -> {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Button(onClick = {
                                        articles.retry()
                                    }, content = {
                                        Text("重新加载")
                                    })
                                }
                            }
                        }

                        is LoadState.Loading -> {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }

                        is LoadState.NotLoading -> {
                            if (articles.itemCount == 0) {
                                item {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("没有更多数据了～")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun ArticleItem(data: HomeArticleItemData) {
    Column(
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface)
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