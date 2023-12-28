package org.liupack.wanandroid.ui.user

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.liupack.wanandroid.model.UiState
import org.liupack.wanandroid.model.UiState.Companion.isLoginExpired
import org.liupack.wanandroid.ui.home.LocalNavigatorParent
import org.liupack.wanandroid.ui.login.LoginScreen

@Stable
object UserScreen : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Outlined.Person)
            return remember {
                TabOptions(4u, title = "我的", icon = icon)
            }
        }

    @Composable
    override fun Content() {
        val navigator = LocalNavigatorParent
        val viewModel = getScreenModel<UserViewModel>()
        val userInfo by viewModel.userInfo.collectAsState()
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
                            navigator.push(LoginScreen)
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
}