package org.liupack.wanandroid.ui.project.child

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.cash.paging.LoadStateLoading
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemKey
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.seiko.imageloader.rememberImagePainter
import org.koin.core.parameter.parametersOf
import org.liupack.wanandroid.common.collectAsLazyEmptyPagingItems
import org.liupack.wanandroid.composables.PagingFullLoadLayout
import org.liupack.wanandroid.composables.pagingFooter
import org.liupack.wanandroid.model.entity.HomeArticleItemData

data class ProjectListScreen(
    private val id: Int = 0,
    private val index: UShort = 0u,
    private val title: String = ""
) : Tab {

    override val key: ScreenKey
        get() = id.toString()
    override val options: TabOptions
        @Composable get() = TabOptions(index = index, title = title)

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<ProjectListViewModel> { parametersOf(id) }
        val projectList = viewModel.projectList.collectAsLazyPagingItems()
        val lazyListState =
            if (projectList.itemCount > 0) viewModel.lazyListState else rememberLazyListState()
        PagingFullLoadLayout(modifier = Modifier.fillMaxSize(),
            pagingState = projectList,
            content = {
                ProjectList(
                    modifier = Modifier.fillMaxSize(),
                    projectList = projectList,
                    lazyListState = lazyListState
                )
            })
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun BoxScope.ProjectList(
        modifier: Modifier,
        projectList: LazyPagingItems<HomeArticleItemData> = collectAsLazyEmptyPagingItems(),
        lazyListState: LazyListState,
    ) {
        val refreshState =
            rememberPullRefreshState(refreshing = projectList.loadState.refresh is LoadStateLoading,
                onRefresh = {
                    projectList.refresh()
                })
        LazyColumn(
            modifier = modifier.pullRefresh(refreshState),
            state = lazyListState,
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(projectList.itemCount, key = projectList.itemKey { it.id }) {
                val data = projectList[it]
                if (data != null) {
                    ProjectListItem(data)
                }
            }
            pagingFooter(projectList)
        }
        PullRefreshIndicator(
            state = refreshState,
            refreshing = projectList.loadState.refresh is LoadStateLoading,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }

    @Composable
    private fun ProjectListItem(data: HomeArticleItemData) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(
                1.dp, Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary,
                        MaterialTheme.colorScheme.tertiary
                    )
                )
            ),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp, start = 12.dp, end = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    data.author ?: data.shareUser ?: "", color = MaterialTheme.colorScheme.tertiary
                )
                Text(
                    data.niceDate.orEmpty(),
                    color = MaterialTheme.colorScheme.outlineVariant,
                    style = MaterialTheme.typography.titleSmall
                )
            }
            Row(
                modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val titleColor = MaterialTheme.colorScheme.outlineVariant
                Image(
                    painter = rememberImagePainter(data.envelopePic.orEmpty()),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp).clip(MaterialTheme.shapes.medium)
                        .clipToBounds(),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier.height(100.dp).weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        data.title.orEmpty(),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        data.desc.orEmpty(),
                        fontSize = 12.sp,
                        modifier = Modifier.weight(1f),
                        color = titleColor,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Row(
                modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(data.superChapterName.orEmpty(), style = MaterialTheme.typography.labelMedium)
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
}
