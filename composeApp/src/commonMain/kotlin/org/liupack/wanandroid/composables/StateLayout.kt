package org.liupack.wanandroid.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import org.liupack.wanandroid.common.Logger
import org.liupack.wanandroid.common.collectAsLazyEmptyPagingItems
import org.liupack.wanandroid.model.UiState
import org.liupack.wanandroid.model.UiState.Companion.isLoginExpired

@Composable
fun <T : Any> PagingFullLoadLayout(
    modifier: Modifier = Modifier,
    pagingState: LazyPagingItems<T> = collectAsLazyEmptyPagingItems(),
    content: @Composable BoxScope.() -> Unit
) {
    Logger.d(pagingState.loadState.refresh.toString())
    when (pagingState.loadState.refresh) {
        is LoadStateLoading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is LoadStateError -> {
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

fun <T : Any> LazyListScope.pagingFooter(pagingState: LazyPagingItems<T>) {
    when (val state = pagingState.loadState.append) {
        is LoadState.Error -> {
            item {
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
        }

        is LoadState.Loading -> {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        is LoadState.NotLoading -> {
            if (state.endOfPaginationReached) {
                item {
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


@Composable
fun <T> FullUiStateLayout(
    modifier: Modifier = Modifier,
    uiState: UiState<T>? = null,
    onRetry: () -> Unit = {},
    content: @Composable BoxScope.(T) -> Unit
) {
    if (uiState != null) {
        when (uiState) {
            is UiState.Loading -> {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Exception -> {
                val message =
                    if (uiState.isLoginExpired) "未登录" else uiState.throwable.message
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Button(onClick = onRetry, content = {
                        Text(message ?: "重新加载")
                    })
                }
            }

            is UiState.Failed -> {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Button(onClick = onRetry, content = {
                        Text("重新加载")
                    })
                }
            }

            is UiState.Success -> {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    content(uiState.data)
                }
            }

            null -> {}
        }
    }
}