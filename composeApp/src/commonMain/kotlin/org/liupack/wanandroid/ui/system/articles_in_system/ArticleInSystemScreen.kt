package org.liupack.wanandroid.ui.system.articles_in_system

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import org.liupack.wanandroid.composables.IconBackButton
import org.liupack.wanandroid.model.entity.ArticleInSystemParams
import org.liupack.wanandroid.ui.main.LocalParentNavigator

data class ArticleInSystemScreen(
    val params: ArticleInSystemParams,
) : Screen {
    @Composable
    override fun Content() {
        ArticleInSystemContent(defaultIndex = params.defaultIndex)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ArticleInSystemContent(defaultIndex: Int) {
        val navigator = LocalParentNavigator
        val tabs by remember {
            derivedStateOf {
                params.children.mapIndexed { index, data ->
                    ArticleInSystemTab(
                        id = data.id,
                        index = index.toUShort(),
                        name = data.name
                    )
                }
            }
        }
        TabNavigator(tabs[defaultIndex]) { tabNavigator ->
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary)
                    ) {
                        TopAppBar(
                            title = { Text(params.title) },
                            navigationIcon = {
                                IconBackButton(onClick = { navigator.pop() })
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                            )
                        )
                        ScrollableTabRow(
                            edgePadding = 0.dp,
                            divider = {},
                            indicator = {
                                TabRowDefaults.Indicator(
                                    Modifier.tabIndicatorOffset(it[tabNavigator.current.options.index.toInt()])
                                        .padding(horizontal = 24.dp),
                                    height = 2.dp
                                )
                            },
                            selectedTabIndex = tabNavigator.current.options.index.toInt(),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            tabs.forEach {
                                TabItem(it)
                            }
                        }
                    }

                },
                content = { paddingValues ->
                    Box(
                        modifier = Modifier.fillMaxSize()
                            .padding(top = paddingValues.calculateTopPadding())
                    ) {
                        CurrentTab()
                    }
                })
        }
    }

    @Composable
    private fun TabItem(tab: Tab = ArticleInSystemTab()) {
        val tabNavigator = LocalTabNavigator.current
        Tab(
            selected = tabNavigator.current == tab,
            onClick = { tabNavigator.current = tab },
            selectedContentColor = MaterialTheme.colorScheme.primary,
            text = { Text(tab.options.title) })
    }
}
