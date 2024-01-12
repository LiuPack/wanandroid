package org.liupack.wanandroid.ui.system.articles_in_system

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.query
import org.koin.core.parameter.parametersOf
import org.liupack.wanandroid.common.RouterKey
import org.liupack.wanandroid.composables.IconBackButton
import org.liupack.wanandroid.composables.PagingFullLoadLayout
import org.liupack.wanandroid.composables.pagingFooter
import org.liupack.wanandroid.model.entity.HomeArticleItemData
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
    val viewModel = koinViewModel(ArticleInSystemViewModel::class) { parametersOf(id) }
    val articleState = viewModel.articleState.collectAsLazyPagingItems()
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(title = { Text(title ?: "文章") },
            navigationIcon = { IconBackButton(onClick = { navigator.goBack() }) })
    }, content = { paddingValues ->
        PagingFullLoadLayout(modifier = Modifier.fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding()),
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
                                    ArticleItem(data)
                                }
                            })
                        pagingFooter(articleState)
                    })
            })
    })
}

@Composable
private fun ArticleItem(data: HomeArticleItemData, onClick: (HomeArticleItemData) -> Unit = {}) {
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