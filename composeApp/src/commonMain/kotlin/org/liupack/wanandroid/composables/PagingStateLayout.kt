package org.liupack.wanandroid.composables

import androidx.compose.foundation.layout.Box
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
import app.cash.paging.compose.LazyPagingItems

@Composable
fun <T : Any> PagingFullLoadLayout(
    modifier: Modifier = Modifier,
    pagingState: LazyPagingItems<T>,
    content: @Composable () -> Unit
) {
    when (pagingState.loadState.refresh) {
        is LoadState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Button(onClick = {
                    pagingState.refresh()
                }, content = {
                    Text("重新加载")
                })
            }
        }

        is LoadState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is LoadState.NotLoading -> {
            if (pagingState.itemCount == 0) {
                Box(modifier = modifier.fillMaxSize()) {
                    Text("没有数据")
                }
            } else {
                content()
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