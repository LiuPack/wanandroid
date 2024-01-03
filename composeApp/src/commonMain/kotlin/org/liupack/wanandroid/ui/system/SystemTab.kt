package org.liupack.wanandroid.ui.system

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ViewKanban
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.liupack.wanandroid.composables.FullUiStateLayout
import org.liupack.wanandroid.model.entity.ArticleInSystemParams
import org.liupack.wanandroid.model.entity.SystemBaseData
import org.liupack.wanandroid.ui.main.LocalParentNavigator
import org.liupack.wanandroid.ui.system.articles_in_system.ArticleInSystemScreen

typealias OnSystemItemClick = (title: String, defaultIndex: Int, children: List<SystemBaseData>) -> Unit

data object SystemTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Outlined.ViewKanban)
            return remember {
                TabOptions(
                    index = 1u,
                    title = "体系",
                    icon = icon
                )
            }
        }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalParentNavigator
        val viewModel = getScreenModel<SystemViewModel>()
        LaunchedEffect(viewModel) {
            viewModel.dispatch(SystemAction.Init)
        }
        val uiState by viewModel.systemBaseData.collectAsState()
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            TopAppBar(
                title = { Text("知识体系") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                )
            )
        }, content = { paddingValues ->
            FullUiStateLayout(
                modifier = Modifier.fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding()),
                uiState = uiState,
                onRetry = {
                    viewModel.dispatch(SystemAction.Refresh)
                },
                content = { dataList ->
                    val lazyListState = viewModel.lazyListState
                    LazyColumn(modifier = Modifier.fillMaxSize(),
                        state = lazyListState,
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        contentPadding = PaddingValues(12.dp),
                        content = {
                            items(dataList, key = { it.id }) { data ->
                                SystemBaseItem(
                                    data = data,
                                    onClick = { title, defaultIndex, children ->
                                        navigator.push(
                                            ArticleInSystemScreen(
                                                ArticleInSystemParams(
                                                    title = title,
                                                    defaultIndex = defaultIndex,
                                                    children = children
                                                )
                                            )
                                        )
                                    })
                            }
                        })
                })
        })
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    private fun SystemBaseItem(
        data: SystemBaseData,
        onClick: OnSystemItemClick? = null
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            content = {
                Text(
                    text = data.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                if (!data.children.isNullOrEmpty()) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        data.children.forEachIndexed { index, child ->
                            Box(
                                modifier = Modifier.wrapContentSize().clip(CircleShape).background(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = CircleShape
                                ).clickable {
                                    onClick?.invoke(data.name, index, data.children)
                                }.padding(6.dp),
                                contentAlignment = Alignment.Center,
                                content = {
                                    Text(text = child.name, fontSize = 14.sp)
                                }
                            )
                        }
                    }
                }
            })
    }
}