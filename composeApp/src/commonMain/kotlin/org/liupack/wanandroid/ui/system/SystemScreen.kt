package org.liupack.wanandroid.ui.system

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowCircleRight
import androidx.compose.material.icons.outlined.ViewKanban
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.liupack.wanandroid.composables.FullUiStateLayout
import org.liupack.wanandroid.model.entity.SystemBaseData

@Stable
object SystemScreen : Tab {

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
        val viewModel = getScreenModel<SystemViewModel>()
        LaunchedEffect(viewModel) {
            viewModel.dispatch(SystemAction.Init)
        }
        val uiState by viewModel.systemBaseData.collectAsState()
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            TopAppBar(title = { Text("知识体系") })
        }, content = { paddingValues ->
            FullUiStateLayout(
                modifier = Modifier.fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding()),
                uiState = uiState,
                onRetry = {
                    viewModel.dispatch(SystemAction.Refresh)
                },
                content = { dataList ->
                    LazyColumn(modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(12.dp),
                        content = {
                            items(dataList, key = { it.id }) { data ->
                                SystemBaseItem(data)
                            }
                        })
                })
        })
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    private fun SystemBaseItem(data: SystemBaseData? = null) {
        data?.let {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.large)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        data.name.orEmpty(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        data.children?.take(6)?.forEach {
                            Text(
                                text = it.name.orEmpty(),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                        }
                    }
                }
                Icon(imageVector = Icons.Outlined.ArrowCircleRight, contentDescription = null)
            }
        }
    }
}