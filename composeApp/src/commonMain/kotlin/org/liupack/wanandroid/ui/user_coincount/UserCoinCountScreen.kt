package org.liupack.wanandroid.ui.user_coincount

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ViewKanban
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import org.liupack.wanandroid.common.collectAsLazyEmptyPagingItems
import org.liupack.wanandroid.composables.IconBackButton
import org.liupack.wanandroid.composables.PagingFullLoadLayout
import org.liupack.wanandroid.composables.pagingFooter
import org.liupack.wanandroid.model.entity.UserCoinCountData
import org.liupack.wanandroid.model.entity.UserCoinCountListData
import org.liupack.wanandroid.ui.coin_count_ranking.CoinCountRankingScreen
import org.liupack.wanandroid.ui.home.LocalNavigatorParent
import kotlin.math.roundToInt

data object UserCoinCountScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigatorParent
        val viewModel = getScreenModel<UserCoinCountViewModel>()
        LaunchedEffect(viewModel) {
            viewModel.dispatch(UserCoinCountAction.Refresh)
        }
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
        val userCoinCountState by viewModel.userCoinCountState.collectAsState()
        val userCoinCountListState = viewModel.userCoinCountList.collectAsLazyPagingItems()
        val toRanking by viewModel.toRankingState.collectAsState(null)
        toRanking?.let {
            if (it) {
                navigator.push(CoinCountRankingScreen)
            }
        }
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            LargeTopAppBar(
                title = { TitleCoinCount(userCoinCountState) },
                actions = {
                    IconButton(onClick = {
                        viewModel.dispatch(UserCoinCountAction.ToRanking)
                    }, content = {
                        Icon(
                            imageVector = Icons.Outlined.ViewKanban,
                            contentDescription = null,
                            modifier = Modifier.rotate(180f)
                        )
                    })
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = { IconBackButton(onClick = { navigator.pop() }) },
                scrollBehavior = scrollBehavior,
            )
        }, content = {
            PagingFullLoadLayout(
                modifier = Modifier.fillMaxSize().padding(it),
                pagingState = userCoinCountListState,
                content = {
                    UserCoinCountContent(
                        modifier = Modifier.fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        userCoinCountList = userCoinCountListState,
                    )
                })
        })
    }

    @Composable
    private fun TitleCoinCount(userCoinCountState: UserCoinCountData) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val animatable = remember { Animatable(0f) }
            LaunchedEffect(userCoinCountState.coinCount) {
                animatable.animateTo(
                    targetValue = userCoinCountState.coinCount.toFloat(),
                    animationSpec = tween(1000)
                )
            }
            Text(text = "我的积分👉")
            Text(
                text = animatable.value.roundToInt().toString(),
                fontWeight = FontWeight.Bold
            )
        }
    }

    @Composable
    private fun UserCoinCountContent(
        modifier: Modifier = Modifier,
        userCoinCountList: LazyPagingItems<UserCoinCountListData> = collectAsLazyEmptyPagingItems(),
    ) {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(userCoinCountList.itemCount) { index ->
                val data = userCoinCountList[index]
                if (data != null) {
                    UserCoinCountItem(data)
                }
            }
            pagingFooter(userCoinCountList)
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun UserCoinCountItem(data: UserCoinCountListData) {
        ListItem(
            modifier = Modifier.fillMaxWidth(), text = {
                Text(text = data.desc, maxLines = 2)
            }, secondaryText = {
                Text(
                    text = data.dateFormatString,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }, trailing = {
                Text(
                    text = data.coinCount.toString(),
                    color = MaterialTheme.colorScheme.primary,
                )
            })
    }
}