package org.liupack.wanandroid.ui.coin_count_ranking

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.cash.paging.LoadStateLoading
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemKey
import com.lt.compose_views.refresh_layout.PullToRefresh
import com.lt.compose_views.refresh_layout.RefreshContentStateEnum
import com.lt.compose_views.refresh_layout.rememberRefreshLayoutState
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.liupack.wanandroid.composables.CustomPullToRefreshContent
import org.liupack.wanandroid.composables.IconBackButton
import org.liupack.wanandroid.composables.PagingFullLoadLayout
import org.liupack.wanandroid.composables.pagingFooter
import org.liupack.wanandroid.model.entity.CoinCountRankingData
import org.liupack.wanandroid.router.Router

fun RouteBuilder.coinCountRankingScreen(navigator: Navigator) {
    scene(
        route = Router.CoinCountRanking.path,
        navTransition = NavTransition(
            createTransition = expandHorizontally(),
            destroyTransition = shrinkHorizontally(),
        )
    ) {
        CoinCountRankingScreen(navigator = navigator)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinCountRankingScreen(navigator: Navigator) {
    val viewModel = koinViewModel(CoinCountRankingViewModel::class)
    val coinCountRankingState = viewModel.coinCountRankingState.collectAsLazyPagingItems()
    val refreshLayoutState = rememberRefreshLayoutState {
        coinCountRankingState.refresh()
    }
    LaunchedEffect(coinCountRankingState.loadState.refresh) {
        val refreshState =
            if (coinCountRankingState.loadState.refresh is LoadStateLoading) RefreshContentStateEnum.Refreshing else RefreshContentStateEnum.Stop
        refreshLayoutState.setRefreshState(refreshState)
    }
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(title = { Text("积分排行榜") }, navigationIcon = {
            IconBackButton(onClick = {
                navigator.goBack()
            })
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = contentColorFor(MaterialTheme.colorScheme.background),
            actionIconContentColor = contentColorFor(MaterialTheme.colorScheme.background),
            navigationIconContentColor = contentColorFor(MaterialTheme.colorScheme.background),
        )
        )
    }, content = { paddingValues ->
        PullToRefresh(refreshLayoutState = refreshLayoutState,
            modifier = Modifier.fillMaxSize().padding(top = paddingValues.calculateTopPadding()),
            refreshContent = remember { { CustomPullToRefreshContent() } }) {
            PagingFullLoadLayout(
                modifier = Modifier.fillMaxSize(),
                pagingState = coinCountRankingState,
                content = {
                    LazyColumn(modifier = Modifier.fillMaxSize(), content = {
                        items(coinCountRankingState.itemCount,
                            key = coinCountRankingState.itemKey { it.userId }) { index ->
                            val data = coinCountRankingState[index]
                            if (data != null) {
                                CoinCountRankingItem(
                                    data = data, max = coinCountRankingState[0]?.coinCount ?: 0
                                )
                            }
                        }
                        pagingFooter(pagingState = coinCountRankingState)
                    })
                },
            )
        }
    })
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CoinCountRankingItem(data: CoinCountRankingData, max: Int) {
    val scope = rememberCoroutineScope()
    val widthFraction by remember { derivedStateOf { if (max == 0) 1f else data.coinCount / max.toFloat() } }
    val color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
    val animatable = remember(data.userId) { Animatable(0f) }
    DisposableEffect(widthFraction) {
        scope.launch {
            animatable.animateTo(widthFraction, animationSpec = tween(1000))
        }
        onDispose {
            scope.launch {
                animatable.stop()
            }.invokeOnCompletion { scope.cancel() }
        }
    }
    Row(
        modifier = Modifier.fillMaxWidth().drawWithContent {
            drawRect(
                color = color, size = size.copy(this.size.width * animatable.value)
            )
            drawContent()
        }.padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Chip(content = {
            Text(text = data.rankDesc, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }, onClick = {})
        Text(text = data.username, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(
            text = "${data.coinCount}",
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.error
        )
    }
}