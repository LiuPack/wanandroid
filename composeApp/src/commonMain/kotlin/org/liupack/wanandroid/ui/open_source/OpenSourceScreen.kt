package org.liupack.wanandroid.ui.open_source

import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.BackStackEntry
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.liupack.wanandroid.composables.FullUiStateLayout
import org.liupack.wanandroid.composables.IconBackButton
import org.liupack.wanandroid.openUrl
import org.liupack.wanandroid.router.Router

fun RouteBuilder.openSource(navigator: Navigator) {
    scene(
        route = Router.OpenSource.path, navTransition = NavTransition(
            createTransition = expandHorizontally(),
            destroyTransition = shrinkHorizontally(),
        )
    ) { backStackEntry ->
        OpenSourceScreen(navigator = navigator, backStackEntry = backStackEntry)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun OpenSourceScreen(navigator: Navigator, backStackEntry: BackStackEntry) {
    val viewModel = koinViewModel(OpenSourceViewModel::class)
    val libraryState by viewModel.libraries.collectAsState(null)
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            navigationIcon = { IconBackButton(onClick = { navigator.goBack() }) },
            title = { Text("开源项目") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = contentColorFor(MaterialTheme.colorScheme.background),
                actionIconContentColor = contentColorFor(MaterialTheme.colorScheme.background),
                navigationIconContentColor = contentColorFor(MaterialTheme.colorScheme.background),
            ),
        )
    }) { paddingValues ->
        FullUiStateLayout(
            modifier = Modifier.fillMaxSize().padding(top = paddingValues.calculateTopPadding()),
            uiState = libraryState
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                it.forEach { listEntry ->
                    stickyHeader {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                                .background(MaterialTheme.colorScheme.tertiary)
                                .padding(12.dp),
                        ) {
                            Text(
                                text = listEntry.key,
                                color = contentColorFor(MaterialTheme.colorScheme.tertiary)
                            )
                        }
                    }
                    items(listEntry.value, key = { library -> library.uniqueId }) { library ->
                        ListItem(modifier = Modifier.fillMaxWidth()
                            .background(MaterialTheme.colorScheme.tertiaryContainer).clickable {
                                openUrl(library.website)
                            },
                            headlineContent = {
                                Text(
                                    text = library.name,
                                    modifier = Modifier.padding(vertical = 6.dp),
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            overlineContent = {
                                Text(text = buildAnnotatedString {
                                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.tertiary)) {
                                        append("Developers：")
                                    }
                                    append(library.developers.joinToString("、") { it.name.orEmpty() })
                                }, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            },
                            supportingContent = {
                                Text(text = library.uniqueId)
                            },
                            trailingContent = {
                                Text(text = library.artifactVersion.orEmpty())
                            })
                    }
                }
            }
        }
    }
}