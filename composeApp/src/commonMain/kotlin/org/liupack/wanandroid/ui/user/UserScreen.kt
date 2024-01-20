package org.liupack.wanandroid.ui.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.BackHandler
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.jetbrains.compose.resources.painterResource
import org.liupack.wanandroid.composables.FullUiStateLayout
import org.liupack.wanandroid.composables.LoadingDialog
import org.liupack.wanandroid.composables.MessageDialog
import org.liupack.wanandroid.model.UiState
import org.liupack.wanandroid.model.UiState.Companion.isLoginExpired
import org.liupack.wanandroid.model.UiState.Companion.successDataOrNull
import org.liupack.wanandroid.model.entity.UserFullInfoData
import org.liupack.wanandroid.model.entity.UserNavigator
import org.liupack.wanandroid.platform.exitApp
import org.liupack.wanandroid.router.Router

fun RouteBuilder.userScreen(navigator: Navigator) {
    scene(route = Router.User.path, navTransition = NavTransition()) {
        BackHandler { exitApp() }
        UserScreen(navigator = navigator)
    }
}

@Composable
private fun UserScreen(navigator: Navigator) {
    val viewModel = koinViewModel(UserViewModel::class)
    val userInfoState by viewModel.userInfoState.collectAsState()
    val loginState by viewModel.toLogin.collectAsState(null)
    val logoutState by viewModel.logout.collectAsState(null)
    if (loginState == true) {
        LaunchedEffect(viewModel.hashCode()) {
            val result = navigator.navigateForResult(
                route = Router.Login.path,
                options = NavOptions(launchSingleTop = true)
            )
            if (result == true) {
                viewModel.dispatch(UserAction.Refresh)
            }
        }
    }
    logoutState?.let { uiState ->
        logoutUiState(
            uiState = uiState,
            refresh = { viewModel.dispatch(UserAction.Refresh) },
        )
    }
    LaunchedEffect(viewModel.hashCode()) {
        viewModel.dispatch(UserAction.Refresh)
    }
    UserInfoContent(
        userInfoState = userInfoState,
        userNavigator = viewModel.userNavigator,
        needLogin = { viewModel.dispatch(UserAction.Login) },
        refresh = { viewModel.dispatch(UserAction.Refresh) },
        itemClick = {
            when (it) {
                UserNavigator.AboutUser -> {
                    viewModel.dispatch(UserAction.OpenUser)
                }

                UserNavigator.Logout -> {
                    viewModel.dispatch(UserAction.Logout)
                }

                UserNavigator.SystemSetting -> {
                    navigator.navigate(
                        route = Router.Setting.path,
                        options = NavOptions(launchSingleTop = true)
                    )
                }

                UserNavigator.UserCoinCount -> {
                    navigator.navigate(
                        route = Router.UserCoinCount.path,
                        options = NavOptions(launchSingleTop = true)
                    )
                }

                UserNavigator.UserFavorite -> {
                    navigator.navigate(
                        route = Router.UserFavorite.path,
                        options = NavOptions(launchSingleTop = true)
                    )
                }

                UserNavigator.UserShared -> {
                    navigator.navigate(
                        route = Router.UserShared.path,
                        options = NavOptions(launchSingleTop = true)
                    )
                }
            }
        },
    )
}

@Composable
private fun logoutUiState(uiState: UiState<Boolean>, refresh: () -> Unit = {}) {
    when (uiState) {
        is UiState.Exception -> {
            MessageDialog(true, uiState.throwable.message.orEmpty())
        }

        is UiState.Failed -> {
            MessageDialog(true, uiState.message)
        }

        is UiState.Loading -> {
            LoadingDialog(true)
        }

        is UiState.Success -> {
            MessageDialog(true, "退出登录成功", hide = refresh)
        }
    }
}

@Composable
private fun UserInfoContent(
    userInfoState: UiState<UserFullInfoData?>? = null,
    userNavigator: List<UserNavigator> = emptyList(),
    itemClick: (UserNavigator) -> Unit = {},
    needLogin: () -> Unit = {},
    refresh: () -> Unit = {},
) {
    Scaffold {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().aspectRatio(2f).background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary,
                                MaterialTheme.colorScheme.tertiary,
                            )
                        )
                    ).padding(WindowInsets.safeDrawing.asPaddingValues()),
                    contentAlignment = Alignment.Center
                ) {
                    FullUiStateLayout(
                        modifier = Modifier.matchParentSize(),
                        uiState = userInfoState,
                        onRetry = {
                            if (userInfoState?.isLoginExpired == true) {
                                needLogin.invoke()
                            } else {
                                refresh.invoke()
                            }
                        },
                        content = { data ->
                            if (data != null) {
                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                        .wrapContentSize().align(Alignment.Center),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    //https://www.iconfont.cn/illustrations/detail?spm=a313x.illustrations_index.i1.d9df05512.6ae13a81mzJ56W&cid=47401
                                    Image(
                                        painter = painterResource("android_studio.png"),
                                        contentDescription = null,
                                        modifier = Modifier.padding(vertical = 12.dp).size(50.dp)
                                            .clip(CircleShape).border(
                                                width = 2.dp,
                                                color = MaterialTheme.colorScheme.onPrimary,
                                                shape = CircleShape
                                            )
                                    )
                                    Text(
                                        text = data.userInfoData?.username ?: "",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    )
                                    Text(
                                        text = "ID:${data.coinInfoData?.userId ?: 0}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    )
                                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                        Text(
                                            text = "等级:${data.coinInfoData?.level ?: 0}",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = MaterialTheme.colorScheme.onPrimary
                                            )
                                        )
                                        Text(
                                            text = "排名: ${data.coinInfoData?.rank ?: 0}",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = MaterialTheme.colorScheme.onPrimary
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }
            items(userNavigator) { navigator ->
                ListItem(modifier = Modifier.fillMaxWidth()
                    .clickable(enabled = userInfoState?.successDataOrNull != null) {
                        itemClick.invoke(navigator)
                    },
                    leadingContent = {
                        Icon(
                            imageVector = navigator.icon,
                            contentDescription = null
                        )
                    },
                    trailingContent = {
                        Icon(
                            imageVector = Icons.Rounded.KeyboardArrowRight,
                            contentDescription = null
                        )
                    },
                    headlineContent = {
                        Text(navigator.name, fontWeight = FontWeight.Bold)
                    })
            }
        }
    }
}