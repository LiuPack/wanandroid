package org.liupack.wanandroid.ui.user

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator
import org.liupack.wanandroid.model.UiState
import org.liupack.wanandroid.model.UiState.Companion.isLoginExpired
import org.liupack.wanandroid.router.Router

@Composable
fun UserScreen(navigator: Navigator = rememberNavigator()) {
    val viewModel = koinViewModel(UserViewModel::class)
    val userInfo by viewModel.userInfo.collectAsState()
    val scope = rememberCoroutineScope()
    LaunchedEffect(viewModel) {
        viewModel.userInfo()
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (val state = userInfo) {
            is UiState.Loading -> {
                CircularProgressIndicator()
            }

            is UiState.Exception -> {
                Button(onClick = {
                    if (state.isLoginExpired) {
                        scope.launch {
                            val isRefresh = navigator.navigateForResult(
                                Router.Login.path, NavOptions(launchSingleTop = true)
                            )
                            if (isRefresh == true) {
                                viewModel.userInfo()
                            }
                        }
                    } else {
                        viewModel.userInfo()
                    }
                }, content = {
                    Text(state.throwable.message.orEmpty())
                })
            }

            is UiState.Failed -> {
                Button(onClick = {
                    viewModel.userInfo()
                }, content = {
                    Text(state.message)
                })
            }

            is UiState.Success -> {
                Text(Json.encodeToString(state.data))
            }
        }
    }
}