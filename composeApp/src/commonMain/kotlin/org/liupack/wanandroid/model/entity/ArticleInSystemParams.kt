package org.liupack.wanandroid.model.entity

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

/**
 * 知识体系跳转体系文章传递的参数
 *
 * @property title 知识体系大标题
 * @property defaultIndex 知识体系小标题选中的下标
 * @property children 知识体系的小标题
 * @constructor Create empty Article in system params
 */
@Stable
@Serializable
data class ArticleInSystemParams(
    val title: String,
    val defaultIndex: Int,
    val children: List<SystemBaseData>
)