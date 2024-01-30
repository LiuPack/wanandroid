package org.liupack.wanandroid.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import app.cash.paging.LoadStateError
import app.cash.paging.LoadStateLoading
import app.cash.paging.LoadStateNotLoading
import app.cash.paging.compose.LazyPagingItems
import org.liupack.wanandroid.common.collectAsLazyEmptyPagingItems
import org.liupack.wanandroid.model.UiState
import org.liupack.wanandroid.model.UiState.Companion.isLoginExpired
import org.liupack.wanandroid.network.exception.LoginExpiredException

@Composable
fun <T : Any> PagingFullLoadLayout(
    modifier: Modifier = Modifier,
    pagingState: LazyPagingItems<T> = collectAsLazyEmptyPagingItems(),
    transitionSpec: AnimatedContentTransitionScope<LazyPagingItems<T>>.() -> ContentTransform = {
        fadeIn() togetherWith fadeOut()
    },
    loginAction: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    AnimatedContent(
        targetState = pagingState, transitionSpec = transitionSpec, modifier = modifier
    ) {
        when (val state = pagingState.loadState.refresh) {
            is LoadStateLoading -> {
                if (pagingState.itemCount == 0) {
                    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        content()
                    }
                }
            }

            is LoadStateError -> {
                if (state.error is LoginExpiredException) {
                    loginAction.invoke()
                }
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Button(onClick = {
                        pagingState.refresh()
                    }, content = {
                        Text("重新加载")
                    })
                }
            }

            is LoadStateNotLoading -> {
                if (pagingState.itemCount == 0) {
                    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("没有数据")
                    }
                } else {
                    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        content()
                    }
                }
            }
        }
    }
}

fun <T : Any> LazyListScope.pagingFooter(pagingState: LazyPagingItems<T>) {
    item {
        AnimatedContent(
            targetState = pagingState,
            modifier = Modifier.wrapContentSize(),
            transitionSpec = {
                expandVertically() togetherWith shrinkVertically()
            }) {
            when (val state = pagingState.loadState.append) {
                is LoadState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                    ) {
                        Button(onClick = {
                            pagingState.retry()
                        }, content = {
                            Text("重新加载")
                        })
                    }
                }

                is LoadState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is LoadState.NotLoading -> {
                    if (state.endOfPaginationReached) {
                        Box(
                            modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                        ) {
                            Text("没有更多数据了～")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun <T> FullUiStateLayout(
    modifier: Modifier = Modifier,
    uiState: UiState<T>? = null,
    transitionSpec: AnimatedContentTransitionScope<UiState<T>?>.() -> ContentTransform = {
        fadeIn() togetherWith fadeOut()
    },
    onRetry: () -> Unit = {},
    loginContent: (@Composable (String) -> Unit)? = null,
    content: @Composable BoxScope.(T) -> Unit
) {
    AnimatedContent(targetState = uiState,
        transitionSpec = transitionSpec,
        modifier = modifier,
        content = {
            when (uiState) {
                is UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is UiState.Exception -> {
                    if (uiState.isLoginExpired) {
                        loginContent?.invoke("去登录") ?: Box(
                            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                        ) {
                            Button(onClick = onRetry, content = {
                                Text("去登录")
                            })
                        }
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                        ) {
                            Button(onClick = onRetry, content = {
                                Text(uiState.throwable.message ?: "重新加载")
                            })
                        }
                    }
                }

                is UiState.Failed -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Button(onClick = onRetry, content = {
                            Text("重新加载")
                        })
                    }
                }

                is UiState.Success -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        content(uiState.data)
                    }
                }

                null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("$uiState is NULL")
                    }
                }
            }
        })
}