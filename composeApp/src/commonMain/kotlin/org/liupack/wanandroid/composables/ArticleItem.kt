package org.liupack.wanandroid.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.liupack.wanandroid.model.entity.HomeArticleItemData

@Composable
fun ArticleItem(data: HomeArticleItemData, onClick: (HomeArticleItemData) -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxWidth().clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.large)
            .clickable { onClick.invoke(data) }.padding(12.dp).clipToBounds(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = data.title,
            maxLines = 2,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = data.shareUser.orEmpty().ifEmpty { data.author.orEmpty() },
            color = MaterialTheme.colorScheme.primary,
            fontSize = MaterialTheme.typography.titleSmall.fontSize,
            modifier = Modifier.align(Alignment.End)
        )
        Text(
            text = data.niceDate.orEmpty().ifEmpty { data.niceShareDate.orEmpty() },
            color = MaterialTheme.colorScheme.outline,
            fontSize = MaterialTheme.typography.titleSmall.fontSize,
            modifier = Modifier.align(Alignment.End),
        )
    }
}