package org.liupack.wanandroid.model.entity

import androidx.compose.runtime.Stable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class BannerData(
    @SerialName("desc")
    val desc: String?,
    @SerialName("id")
    val id: Int?,
    @SerialName("imagePath")
    val imagePath: String?,
    @SerialName("isVisible")
    val isVisible: Int?,
    @SerialName("order")
    val order: Int?,
    @SerialName("title")
    val title: String?,
    @SerialName("type")
    val type: Int?,
    @SerialName("url")
    val url: String?
)