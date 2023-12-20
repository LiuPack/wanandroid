package org.liupack.wanandroid.model.entity


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectSortData(
    @SerialName("author")
    val author: String,
    @SerialName("courseId")
    val courseId: Int,
    @SerialName("cover")
    val cover: String,
    @SerialName("desc")
    val desc: String,
    @SerialName("id")
    val id: Int,
    @SerialName("lisense")
    val lisense: String,
    @SerialName("lisenseLink")
    val lisenseLink: String,
    @SerialName("name")
    val name: String,
    @SerialName("order")
    val order: Int,
    @SerialName("parentChapterId")
    val parentChapterId: Int,
    @SerialName("type")
    val type: Int,
    @SerialName("userControlSetTop")
    val userControlSetTop: Boolean,
    @SerialName("visible")
    val visible: Int,
)