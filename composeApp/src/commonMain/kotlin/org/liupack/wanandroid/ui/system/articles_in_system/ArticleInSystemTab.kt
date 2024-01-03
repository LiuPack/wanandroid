package org.liupack.wanandroid.ui.system.articles_in_system

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemKey
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.koin.core.parameter.parametersOf
import org.liupack.wanandroid.composables.PagingFullLoadLayout
import org.liupack.wanandroid.composables.pagingFooter
import org.liupack.wanandroid.model.entity.HomeArticleItemData

data class ArticleInSystemTab(
    val id: Int = 0,
    val index: UShort = 0u,
    val name: String = ""
) : Tab {

    override val key: ScreenKey
        get() = id.toString()

    override val options: TabOptions
        @Composable
        get() = TabOptions(index = index, title = name)

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<ArticleInSystemViewModel> { parametersOf(id) }
        val articleState = viewModel.articleState.collectAsLazyPagingItems()
        PagingFullLoadLayout(
            modifier = Modifier.fillMaxSize(),
            pagingState = articleState,
            content = {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(12.dp),
                    content = {
                        items(
                            articleState.itemCount,
                            key = articleState.itemKey { it.id },
                            itemContent = { index ->
                                val data = articleState[index]
                                if (data != null) {
                                    ArticleItem(data)
                                }
                            })
                        pagingFooter(articleState)
                    }
                )
            })

    }

    @Composable
    private fun ArticleItem(data: HomeArticleItemData) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = data.author.orEmpty().ifEmpty { data.shareUser.orEmpty() },
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = data.niceDate.orEmpty(),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Text(
                text = data.title.orEmpty(),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(buildAnnotatedString {
                    withStyle(SpanStyle(fontSize = 12.sp)) {
                        append(data.superChapterName.orEmpty())
                        append("·")
                        append(data.chapterName.orEmpty())
                    }
                }, modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    modifier = Modifier.clip(CircleShape).clickable { }.padding(4.dp)
                )
            }
        }
    }
}