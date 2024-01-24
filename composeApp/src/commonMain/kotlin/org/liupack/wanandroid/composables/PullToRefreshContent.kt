package org.liupack.wanandroid.composables

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowCircleDown
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.lt.compose_views.refresh_layout.RefreshContentStateEnum
import com.lt.compose_views.refresh_layout.RefreshLayoutState
import kotlin.math.abs

@Composable
fun RefreshLayoutState.CustomPullToRefreshContent() {
    val refreshContentState by remember {
        getRefreshContentState()
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
    ) {
        when (refreshContentState) {
            RefreshContentStateEnum.Stop -> {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = "",
                    modifier = Modifier
                        .size(24.dp)
                )
            }

            RefreshContentStateEnum.Refreshing -> {
                //循环旋转动画
                val infiniteTransition = rememberInfiniteTransition()
                val rotate by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 1000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    )
                )
                Icon(
                    imageVector = Icons.Outlined.Refresh,
                    contentDescription = "",
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(rotate)
                )
            }

            RefreshContentStateEnum.Dragging -> {
                //旋转动画
                val isCannotRefresh =
                    abs(getRefreshContentOffset()) < getRefreshContentThreshold()
                val rotate by animateFloatAsState(targetValue = if (isCannotRefresh) 0f else 180f)
                Icon(
                    imageVector = Icons.Outlined.ArrowCircleDown,
                    contentDescription = "",
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(rotate)
                )
            }
        }
        Text(
            text = when (refreshContentState) {
                RefreshContentStateEnum.Stop -> getRefreshCompleteString()
                RefreshContentStateEnum.Refreshing -> getRefreshingString()
                RefreshContentStateEnum.Dragging -> {
                    if (abs(getRefreshContentOffset()) < getRefreshContentThreshold()) {
                        getDropDownToRefreshString()
                    } else {
                        getReleaseRefreshNowString()
                    }
                }
            },
            color = contentColorFor(MaterialTheme.colorScheme.background)
        )
    }
}

//用户指定的语言
private var Res_language: String? = null
private fun getLanguage(): String {
    Res_language?.let { return it }
    return Locale.current.language
}


@Stable
internal fun getNoMoreDataString(): String {
    return when (getLanguage()) {
        "zh" -> "没有更多数据了"
        else -> "No more data"
    }
}

@Stable
internal fun getLoadingString(): String {
    return when (getLanguage()) {
        "zh" -> "正在加载中…"
        else -> "Loading"
    }
}

@Stable
internal fun getRefreshCompleteString(): String {
    return when (getLanguage()) {
        "zh" -> "刷新完成"
        else -> "Refresh complete"
    }
}

@Stable
internal fun getRefreshingString(): String {
    return when (getLanguage()) {
        "zh" -> "正在刷新…"
        else -> "Refreshing"
    }
}

@Stable
internal fun getDropDownToRefreshString(): String {
    return when (getLanguage()) {
        "zh" -> "下拉可以刷新"
        else -> "Pull to refresh"
    }
}

@Stable
internal fun getReleaseRefreshNowString(): String {
    return when (getLanguage()) {
        "zh" -> "释放立即刷新"
        else -> "Release refresh"
    }
}