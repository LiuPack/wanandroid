package org.liupack.wanandroid.model.entity


import androidx.compose.runtime.Stable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class WechatAccountSortData(
    @SerialName("author")
    val author: String?,
    @SerialName("courseId")
    val courseId: Int?,
    @SerialName("desc")
    val desc: String?,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("order")
    val order: Int?,
    @SerialName("parentChapterId")
    val parentChapterId: Int?,
    @SerialName("type")
    val type: Int?,
    @SerialName("visible")
    val visible: Int?
)