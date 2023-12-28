package org.liupack.wanandroid.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import app.cash.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.flowOf

@Stable
fun <T : Any> emptyPagingFlow() = flowOf(PagingData.from(emptyList<T>()))

@Composable
fun <T : Any> collectAsLazyEmptyPagingItems() = emptyPagingFlow<T>().collectAsLazyPagingItems()