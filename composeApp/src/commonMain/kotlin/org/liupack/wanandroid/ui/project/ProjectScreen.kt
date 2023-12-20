package org.liupack.wanandroid.ui.project

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemKey
import com.lt.compose_views.compose_pager.ComposePager
import com.lt.compose_views.compose_pager.rememberComposePagerState
import com.lt.compose_views.pager_indicator.TextPagerIndicator
import com.seiko.imageloader.rememberImagePainter
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator
import org.liupack.wanandroid.composables.PagingFullLoadLayout
import org.liupack.wanandroid.composables.pagingFooter
import org.liupack.wanandroid.model.entity.HomeArticleItemData


@Composable
fun ProjectScreen(navigator: Navigator = rememberNavigator()) {
    val viewModel = koinViewModel(ProjectViewModel::class)
    LaunchedEffect(viewModel) {
        viewModel.projectSort()
    }
    val pagerState = rememberComposePagerState()
    val projectSortList by viewModel.projectSort.collectAsStateWithLifecycle(emptyList())
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        if (projectSortList.isNotEmpty()) {
            val contents by remember { derivedStateOf { projectSortList.map { it.name } } }
            TextPagerIndicator(
                texts = contents,
                offsetPercentWithSelectFlow = remember { pagerState.createChildOffsetPercentFlow() },
                selectIndexFlow = remember { pagerState.createCurrSelectIndexFlow() },
                fontSize = 14.sp,
                selectFontSize = 16.sp,
                textColor = MaterialTheme.colorScheme.onBackground,
                selectTextColor = MaterialTheme.colorScheme.primary,
                selectIndicatorColor = MaterialTheme.colorScheme.primary,
                onIndicatorClick = {
                    pagerState.setPageIndexWithAnimate(it)
                },
                modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
                    .fillMaxWidth()
                    .height(35.dp),

                margin = 28.dp
            )
        }
    }, content = { paddingValues ->
        if (projectSortList.isNotEmpty()) {
            val currentIndex by pagerState.createCurrSelectIndexFlow()
                .collectAsStateWithLifecycle(0)
            val id = projectSortList[currentIndex].id
            ComposePager(
                pageCount = projectSortList.size,
                composePagerState = pagerState
            ) {
                ProjectListContent(
                    modifier = Modifier.padding(paddingValues),
                    id = id
                )
            }
        }
    })
}

@Composable
fun ProjectListContent(modifier: Modifier, id: Int) {
    val viewModel = koinViewModel(ProjectListViewModel::class, key = id.toString())
    LaunchedEffect(viewModel) {
        viewModel.projectList(id)
    }
    val projectList = viewModel.projectList.collectAsLazyPagingItems()
    PagingFullLoadLayout(
        modifier = modifier,
        pagingState = projectList,
        content = {
            ProjectList(modifier = modifier, id = id, projectList = projectList)
        })
}

@Composable
fun ProjectList(
    modifier: Modifier,
    id: Int,
    projectList: LazyPagingItems<HomeArticleItemData>? = null
) {
    projectList?.let {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                projectList.itemCount,
                key = projectList.itemKey { "$id-${it.id}-${it.userId}" }) {
                projectList[it]?.let { itemData ->
                    ProjectListItem(itemData)
                }
            }
            pagingFooter(projectList)
        }
    }
}

@Composable
fun ProjectListItem(data: HomeArticleItemData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp, start = 12.dp, end = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(data.author ?: data.shareUser ?: "", color = MaterialTheme.colorScheme.tertiary)
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
                modifier = Modifier.size(100.dp)
                    .clip(MaterialTheme.shapes.medium)
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
