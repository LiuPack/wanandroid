package org.liupack.wanandroid.ui.project.child

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.LoadStateLoading
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemKey
import com.lt.compose_views.refresh_layout.PullToRefresh
import com.lt.compose_views.refresh_layout.RefreshContentStateEnum
import com.lt.compose_views.refresh_layout.rememberRefreshLayoutState
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.BackHandler
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import org.koin.core.parameter.parametersOf
import org.liupack.wanandroid.common.RouterKey
import org.liupack.wanandroid.common.collectAsLazyEmptyPagingItems
import org.liupack.wanandroid.common.parametersOf
import org.liupack.wanandroid.composables.ArticleItem
import org.liupack.wanandroid.composables.PagingFullLoadLayout
import org.liupack.wanandroid.composables.pagingFooter
import org.liupack.wanandroid.composables.rememberLazyListState
import org.liupack.wanandroid.model.UiState.Companion.isLoginExpired
import org.liupack.wanandroid.model.UiStateSuccess
import org.liupack.wanandroid.model.entity.HomeArticleItemData
import org.liupack.wanandroid.platform.exitApp
import org.liupack.wanandroid.router.Router


fun RouteBuilder.projectListScreen(
    parentNavigator: Navigator,
    router: String,
    id: Int
) {
    scene(route = router) {
        BackHandler { exitApp() }
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
    val favoriteState by viewModel.favoriteState.collectAsState(null)
    var isFavoriteAction by remember { mutableStateOf(false) }
    favoriteState?.let {
        LaunchedEffect(it) {
            if (it.isLoginExpired) {
                val result = navigator.navigateForResult(
                    Router.Login.path,
                    NavOptions(launchSingleTop = true)
                )
                if (result == true) {
                    projectList.refresh()
                }
            }
            if (favoriteState is UiStateSuccess) {
                isFavoriteAction = true
                projectList.refresh()
            }
        }
    }
    val refreshLayoutState = rememberRefreshLayoutState {
        isFavoriteAction = false
        projectList.refresh()
    }
    LaunchedEffect(projectList.loadState.refresh) {
        val canRefresh = projectList.loadState.refresh is LoadStateLoading && !isFavoriteAction
        refreshLayoutState.setRefreshState(if (canRefresh) RefreshContentStateEnum.Refreshing else RefreshContentStateEnum.Stop)
    }
    PullToRefresh(
        refreshLayoutState = refreshLayoutState,
        modifier = Modifier.fillMaxSize(),
        content = {
            PagingFullLoadLayout(
                modifier = Modifier.fillMaxSize(),
                pagingState = projectList,
                content = {
                    ProjectList(
                        modifier = Modifier.fillMaxSize(),
                        navigator = navigator,
                        projectList = projectList,
                        lazyListState = lazyListState,
                        addFavorite = {
                            viewModel.dispatch(ProjectListAction.Favorite(it.id))
                        }, cancelFavorite = {
                            viewModel.dispatch(ProjectListAction.CancelFavorite(it.id))
                        }
                    )
                })
        },
    )
}

@Composable
private fun ProjectList(
    modifier: Modifier,
    navigator: Navigator,
    projectList: LazyPagingItems<HomeArticleItemData> = collectAsLazyEmptyPagingItems(),
    lazyListState: LazyListState,
    addFavorite: (HomeArticleItemData) -> Unit = {},
    cancelFavorite: (HomeArticleItemData) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier,
        state = lazyListState,
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(projectList.itemCount, key = projectList.itemKey { it.id }) { index ->
            val data = projectList[index]
            if (data != null) {
                ArticleItem(data = data, onClick = {
                    val path = Router.WebView.parametersOf(RouterKey.url to it.link)
                    navigator.navigate(
                        route = path,
                        options = NavOptions(launchSingleTop = true)
                    )
                }, onFavoriteClick = {
                    if (it) {
                        addFavorite.invoke(data)
                    } else {
                        cancelFavorite.invoke(data)
                    }
                })
            }
        }
        pagingFooter(projectList)
    }
}


