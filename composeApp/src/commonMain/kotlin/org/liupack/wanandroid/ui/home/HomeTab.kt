package org.liupack.wanandroid.ui.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemKey
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.liupack.wanandroid.common.Constants
import org.liupack.wanandroid.common.collectAsLazyEmptyPagingItems
import org.liupack.wanandroid.composables.PagingFullLoadLayout
import org.liupack.wanandroid.composables.pagingFooter
import org.liupack.wanandroid.model.entity.HomeArticleItemData
import org.liupack.wanandroid.openUrl

data object HomeTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Outlined.Home)
            return remember {
                TabOptions(
                    index = 0u,
                    title = "首页",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<HomeViewModel>()
        val articleState = viewModel.articles.collectAsLazyPagingItems()
        val lazyListState =
            if (articleState.itemCount > 0) viewModel.lazyListState else rememberLazyListState()
        HomeScreen(articleState = articleState, lazyListState = lazyListState)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun HomeScreen(
        articleState: LazyPagingItems<HomeArticleItemData> = collectAsLazyEmptyPagingItems(),
        lazyListState: LazyListState,
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
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
            },
            content = { paddingValues ->
                PagingFullLoadLayout(
                    modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
                    pagingState = articleState
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(12.dp),
                        state = lazyListState
                    ) {
                        items(count = articleState.itemCount,
                            key = articleState.itemKey { it.id },
                            itemContent = { index ->
                                val item = articleState[index]
                                if (item != null) {
                                    HomeArticleItem(data = item)
                                }
                            })
                        pagingFooter(articleState)
                    }
                }
            }
        )
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    private fun HomeArticleItem(data: HomeArticleItemData) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (data.fresh) {
                    Text(text = "新", modifier = Modifier.padding(8.dp))
                }
                Text(data.niceShareDate.orEmpty())
            }
            Text(data.title.orEmpty())
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FlowRow(modifier = Modifier.weight(1f)) {
                    data.tags?.forEach {
                        Text(it.name.orEmpty(), fontSize = 10.sp)
                    }
                }
                Text(data.superChapterName.orEmpty() + "/" + data.chapterName.orEmpty())
            }
        }
    }
}



