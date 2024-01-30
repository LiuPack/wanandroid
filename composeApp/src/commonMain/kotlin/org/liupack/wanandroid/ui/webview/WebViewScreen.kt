package org.liupack.wanandroid.ui.webview

import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.query
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.liupack.wanandroid.common.Logger
import org.liupack.wanandroid.common.RouterKey
import org.liupack.wanandroid.composables.IconBackButton
import org.liupack.wanandroid.router.Router

fun RouteBuilder.webviewScreen(navigator: Navigator) {
    scene(
        route = Router.WebView.path, navTransition = NavTransition(
            createTransition = expandHorizontally(),
            destroyTransition = shrinkHorizontally(),
        )
    ) {
        val url = it.query<String>(RouterKey.url).orEmpty()
        Logger.i("链接地址：$url")
        WebViewScreen(navigator = navigator, url = url)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun WebViewScreen(navigator: Navigator, url: String) {
    val scope = rememberCoroutineScope()
    var title by rememberSaveable(url) { mutableStateOf("详情") }
    val webViewState = rememberWebViewState(url)
    DisposableEffect(navigator.hashCode()) {
        val job = snapshotFlow { webViewState.pageTitle }.map {
            title = it ?: "详情"
        }.launchIn(scope)
        onDispose {
            job.cancel()
        }
    }
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = { Text(title, maxLines = 1, modifier = Modifier.basicMarquee()) },
            navigationIcon = {
                IconBackButton(onClick = { navigator.goBack() }, imageVector = Icons.Outlined.Close)
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = contentColorFor(MaterialTheme.colorScheme.background),
                actionIconContentColor = contentColorFor(MaterialTheme.colorScheme.background),
                navigationIconContentColor = contentColorFor(MaterialTheme.colorScheme.background),
            ),
        )
    }, content = { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(top = paddingValues.calculateTopPadding())) {
            WebView(
                state = webViewState,
                modifier = Modifier.fillMaxSize(),
                onCreated = {
                }, onDispose = {

                }
            )
            if (webViewState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                    content = {
                        CircularProgressIndicator(modifier = Modifier.size(48.dp))
                    })
            }
        }
    })
}