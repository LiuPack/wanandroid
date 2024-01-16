package org.liupack.wanandroid.ui.project.child

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.cash.paging.LoadStateLoading
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemKey
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import org.koin.core.parameter.parametersOf
import org.liupack.wanandroid.common.RouterKey
import org.liupack.wanandroid.common.collectAsLazyEmptyPagingItems
import org.liupack.wanandroid.common.parametersOf
import org.liupack.wanandroid.composables.PagingFullLoadLayout
import org.liupack.wanandroid.composables.pagingFooter
import org.liupack.wanandroid.composables.rememberLazyListState
import org.liupack.wanandroid.model.entity.HomeArticleItemData
import org.liupack.wanandroid.router.Router


fun RouteBuilder.projectListScreen(
    parentNavigator: Navigator,
    router: String,
    id: Int
) {
    scene(route = router) {
        ProjectListScreen(parentNavigator, id)
    }
}

@Composable
fun ProjectListScreen(navigator: Navigator, id: Int) {
    val viewModel = koinViewModel(vmClass = ProjectListViewModel::class,
        key = id.toString(),
        parameters = { parametersOf(id) })
    val projectList = viewModel.projectList.collectAsLazyPagingItems()
    val lazyListState = projectList.rememberLazyListState()
    PagingFullLoadLayout(modifier = Modifier.fillMaxSize(), pagingState = projectList, content = {
        ProjectList(
            modifier = Modifier.fillMaxSize(),
            navigator = navigator,
            projectList = projectList,
            lazyListState = lazyListState
        )
    })
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BoxScope.ProjectList(
    modifier: Modifier,
    navigator: Navigator,
    projectList: LazyPagingItems<HomeArticleItemData> = collectAsLazyEmptyPagingItems(),
    lazyListState: LazyListState,
) {
    val refreshState = rememberPullRefreshState(
        refreshing = projectList.loadState.refresh is LoadStateLoading,
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
                ArticleItem(data = data, onClick = {
                    val path = Router.WebView.parametersOf(RouterKey.url to it.link)
                    navigator.navigate(route = path, options = NavOptions(launchSingleTop = true))
                })
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
