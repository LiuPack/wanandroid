package org.liupack.wanandroid.ui.system.articles_in_system

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemKey
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.query
import org.koin.core.parameter.parametersOf
import org.liupack.wanandroid.common.RouterKey
import org.liupack.wanandroid.common.parametersOf
import org.liupack.wanandroid.composables.ArticleItem
import org.liupack.wanandroid.composables.IconBackButton
import org.liupack.wanandroid.composables.PagingFullLoadLayout
import org.liupack.wanandroid.composables.pagingFooter
import org.liupack.wanandroid.router.Router

fun RouteBuilder.articleInSystemScreen(navigator: Navigator) {
    scene(Router.ArticleInSystem.path) {
        val title = it.query<String>(RouterKey.title)
        val id = it.query<Int>(RouterKey.id)
        ArticleInSystemScreen(navigator = navigator, title = title, id = id)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArticleInSystemScreen(navigator: Navigator, id: Int?, title: String?) {
    val viewModel = koinViewModel(ArticleInSystemViewModel::class) { parametersOf(id) }
    val articleState = viewModel.articleState.collectAsLazyPagingItems()
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = { Text(title ?: "文章") },
            navigationIcon = { IconBackButton(onClick = { navigator.goBack() }) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = contentColorFor(MaterialTheme.colorScheme.background),
                actionIconContentColor = contentColorFor(MaterialTheme.colorScheme.background),
                navigationIconContentColor = contentColorFor(MaterialTheme.colorScheme.background),
            )
        )
    }, content = { paddingValues ->
        PagingFullLoadLayout(modifier = Modifier.fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding()),
            pagingState = articleState,
            content = {
                LazyColumn(modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(12.dp),
                    content = {
                        items(articleState.itemCount,
                            key = articleState.itemKey { it.id },
                            itemContent = { index ->
                                val data = articleState[index]
                                if (data != null) {
                                    ArticleItem(data, onClick = {
                                        val path =
                                            Router.WebView.parametersOf(RouterKey.url to it.link)
                                        navigator.navigate(path)
                                    })
                                }
                            })
                        pagingFooter(articleState)
                    })
            })
    })
}