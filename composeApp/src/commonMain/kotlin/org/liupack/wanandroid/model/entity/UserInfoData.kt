package org.liupack.wanandroid.model.entity


import androidx.compose.runtime.Stable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class UserInfoData(
    @SerialName("admin")
    val admin: Boolean?,
    @SerialName("chapterTops")
    val chapterTops: List<String>?,
    @SerialName("coinCount")
    val coinCount: Int?,
    @SerialName("collectIds")
    val collectIds: List<Int?>?,
    @SerialName("email")
    val email: String?,
    @SerialName("icon")
    val icon: String?,
    @SerialName("id")
    val id: Int?,
    @SerialName("nickname")
    val nickname: String?,
    @SerialName("password")
    val password: String?,
    @SerialName("publicName")
    val publicName: String?,
    @SerialName("token")
    val token: String?,
    @SerialName("type")
    val type: Int?,
    @SerialName("username")
    val username: String?
)