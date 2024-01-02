package org.liupack.wanandroid.ui.project

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.liupack.wanandroid.ui.project.child.ProjectListScreen

object ProjectScreen : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Outlined.Book)
            return remember {
                TabOptions(index = 3u, title = "项目", icon = icon)
            }
        }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<ProjectViewModel>()
        LaunchedEffect(viewModel) {
            viewModel.projectSort()
        }
        val projectSortList by viewModel.projectSort.collectAsState()
        if (projectSortList.isNotEmpty()) {
            val tabs by remember {
                derivedStateOf {
                    projectSortList.mapIndexed { index, projectSortData ->
                        ProjectListScreen(
                            id = projectSortData.id,
                            index = index.toUShort(),
                            title = projectSortData.name
                        )
                    }.toList()
                }
            }
            TabNavigator(tabs.first()) { tabNavigator ->
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(title = {
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
                        })
                    },
                    content = { paddingValues ->
                        Box(modifier = Modifier.padding(top = paddingValues.calculateTopPadding())) {
                            CurrentTab()
                        }
                    })
            }
        }
    }

    @Composable
    private fun TabItem(tab: Tab = ProjectListScreen()) {
        val tabNavigator = LocalTabNavigator.current
        Tab(
            selected = tabNavigator.current == tab,
            onClick = { tabNavigator.current = tab },
            selectedContentColor = MaterialTheme.colorScheme.primary,
            text = { Text(tab.options.title) })
    }
}