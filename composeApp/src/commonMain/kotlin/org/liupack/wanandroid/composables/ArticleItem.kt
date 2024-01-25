package org.liupack.wanandroid.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.liupack.wanandroid.model.entity.HomeArticleItemData

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArticleItem(
    modifier: Modifier = Modifier,
    data: HomeArticleItemData,
    showPinned: Boolean = false,
    favoriteVisibility: Boolean = true,
    onFavoriteClick: HomeArticleItemData .(Boolean) -> Unit = {},
    onLongClick: (HomeArticleItemData) -> Unit = {},
    onClick: (HomeArticleItemData) -> Unit = {}
) {
    val favoriteState by rememberSaveable(data.id) { mutableStateOf(data.collect ?: false) }
    Column(
        modifier = modifier.fillMaxWidth().clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.large)
            .combinedClickable(onLongClick = { onLongClick.invoke(data) },
                onClick = { onClick.invoke(data) }).padding(12.dp).clipToBounds(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                if (showPinned) {
                    appendInlineContent("pinned", "置顶")
                }
                if (data.fresh) {
                    appendInlineContent("fresh", "新")
                }
                append(data.title)
            },
            maxLines = 2,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            inlineContent = tagMapOf()
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = data.shareUser.orEmpty().ifEmpty { data.author.orEmpty() },
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    modifier = Modifier.align(Alignment.Start)
                )
                Text(
                    text = data.niceDate.orEmpty().ifEmpty { data.niceShareDate.orEmpty() },
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    modifier = Modifier.align(Alignment.Start),
                )
            }
            if (favoriteVisibility) {
                IconToggleButton(
                    checked = favoriteState,
                    onCheckedChange = {
                        onFavoriteClick.invoke(data, it)
                    },
                    content = {
                        Icon(
                            imageVector = if (favoriteState) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                    },
                )
            }
        }

    }
}

private fun tagMapOf() = mapOf("pinned" to InlineTextContent(
    placeholder = Placeholder(
        width = 48.sp,
        height = 20.sp,
        placeholderVerticalAlign = PlaceholderVerticalAlign.Center
    )
) {
    Box(
        modifier = Modifier.padding(end = 4.dp).fillMaxSize().border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.tertiary,
            shape = RoundedCornerShape(2.dp)
        ), contentAlignment = Alignment.Center
    ) {
        Text(
            text = it,
            fontSize = MaterialTheme.typography.titleSmall.fontSize,
            color = MaterialTheme.colorScheme.tertiary,
        )
    }
}, "fresh" to InlineTextContent(
    placeholder = Placeholder(
        width = 24.sp,
        height = 20.sp,
        placeholderVerticalAlign = PlaceholderVerticalAlign.Center
    )
) {
    Box(
        modifier = Modifier.padding(end = 4.dp).fillMaxSize().border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.error,
            shape = RoundedCornerShape(2.dp)
        ), contentAlignment = Alignment.Center
    ) {
        Text(
            text = it,
            fontSize = MaterialTheme.typography.titleSmall.fontSize,
            color = MaterialTheme.colorScheme.error,
        )
    }
})