package org.liupack.wanandroid.ui.user_shared.add_shared

import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.liupack.wanandroid.composables.IconBackButton
import org.liupack.wanandroid.composables.LoadingDialog
import org.liupack.wanandroid.composables.MessageDialog
import org.liupack.wanandroid.model.UiState
import org.liupack.wanandroid.router.Router

fun RouteBuilder.userAddSharedScreen(navigator: Navigator) {
    scene(
        route = Router.UserAddShared.path, navTransition = NavTransition(
            createTransition = expandHorizontally(),
            destroyTransition = shrinkHorizontally(),
        )
    ) {
        UserAddSharedScreen(navigator)
    }
}

@Composable
fun UserAddSharedScreen(navigator: Navigator) {
    val viewModel = koinViewModel(UserAddSharedViewModel::class)
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            UserAddSharedTopAppBar(navigator)
        },
        content = { paddingValues ->
            val scrollState = rememberScrollState()
            val submitState by viewModel.submitShared.collectAsState(null)
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    Column(
                        modifier = Modifier.fillMaxWidth().wrapContentHeight()
                            .padding(horizontal = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val title by viewModel.shareTitle.collectAsState()
                        Text("文章标题", style = MaterialTheme.typography.labelSmall)
                        TextField(
                            value = title,
                            onValueChange = {
                                viewModel.dispatch(UserAddSharedAction.InputTitle(it))
                            },
                            placeholder = {
                                Text(
                                    text = "请输入文章标题",
                                    fontSize = MaterialTheme.typography.labelLarge.fontSize
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next,
                            ),
                            maxLines = 1,
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium,
                            colors = TextFieldDefaults.colors(
                                disabledIndicatorColor = Color.Transparent,
                                errorIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth().wrapContentHeight()
                            .padding(horizontal = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val link by viewModel.shareLink.collectAsState()
                        Text("文章链接", style = MaterialTheme.typography.labelSmall)
                        TextField(
                            value = link,
                            onValueChange = {
                                viewModel.dispatch(UserAddSharedAction.InputLink(it))
                            },
                            placeholder = {
                                Text(text = buildAnnotatedString {
                                    append("请输入文章链接，必须是")
                                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                                        append("http://、https://")
                                    }
                                    append("开头")
                                }, fontSize = MaterialTheme.typography.labelLarge.fontSize)
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Uri,
                                imeAction = ImeAction.Done
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium,
                            colors = TextFieldDefaults.colors(
                                disabledIndicatorColor = Color.Transparent,
                                errorIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )
                    }
                    Button(
                        onClick = {
                            viewModel.dispatch(UserAddSharedAction.Shared)
                        },
                        content = {
                            Text("分享")
                        }, modifier = Modifier.padding(24.dp).fillMaxWidth()
                    )
                }
            )
            when (val state = submitState) {
                is UiState.Loading -> {
                    LoadingDialog(isVisibility = true)
                }

                is UiState.Exception -> {
                    MessageDialog(
                        isVisibility = true,
                        message = state.throwable.stackTraceToString()
                    )
                }

                is UiState.Failed -> {
                    MessageDialog(isVisibility = true, message = state.message)
                }

                is UiState.Success -> {
                    MessageDialog(
                        isVisibility = true, message = state.data,
                        hide = {
                            navigator.goBackWith(true)
                        }
                    )
                }

                else -> {}
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAddSharedTopAppBar(navigator: Navigator) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            navigationIconContentColor = contentColorFor(MaterialTheme.colorScheme.background),
            titleContentColor = contentColorFor(MaterialTheme.colorScheme.background),
            actionIconContentColor = contentColorFor(MaterialTheme.colorScheme.background),
        ),
        title = {
            Text(text = "分享文章")
        },
        navigationIcon = {
            IconBackButton(onClick = { navigator.goBack() })
        },
    )
}