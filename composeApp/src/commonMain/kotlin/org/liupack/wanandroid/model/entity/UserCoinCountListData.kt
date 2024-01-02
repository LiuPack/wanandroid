package org.liupack.wanandroid.model.entity


import androidx.compose.runtime.Stable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.liupack.wanandroid.common.formatGMT8

@Stable
@Serializable
data class UserCoinCountListData(
    @SerialName("coinCount")
    val coinCount: Int,
    @SerialName("date")
    val date: Long,
    @SerialName("desc")
    val desc: String,
    @SerialName("id")
    val id: Int,
    @SerialName("reason")
    val reason: String,
    @SerialName("type")
    val type: Int,
    @SerialName("userId")
    val userId: Int,
    @SerialName("userName")
    val userName: String,
) {
    val dateFormatString: String get() = formatGMT8(date)
}