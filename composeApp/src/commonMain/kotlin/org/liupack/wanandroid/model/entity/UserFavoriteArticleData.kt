package org.liupack.wanandroid.model.entity


import androidx.compose.runtime.Stable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class UserFavoriteArticleData(
    @SerialName("author")
    val author: String?,
    @SerialName("chapterId")
    val chapterId: Int?,
    @SerialName("chapterName")
    val chapterName: String?,
    @SerialName("courseId")
    val courseId: Int?,
    @SerialName("desc")
    val desc: String?,
    @SerialName("envelopePic")
    val envelopePic: String?,
    @SerialName("id")
    val id: Int,
    @SerialName("link")
    val link: String?,
    @SerialName("niceDate")
    val niceDate: String?,
    @SerialName("origin")
    val origin: String?,
    @SerialName("originId")
    val originId: Int?,
    @SerialName("publishTime")
    val publishTime: Long?,
    @SerialName("title")
    val title: String,
    @SerialName("userId")
    val userId: Int?,
    @SerialName("visible")
    val visible: Int?,
    @SerialName("zan")
    val zan: Int?
)