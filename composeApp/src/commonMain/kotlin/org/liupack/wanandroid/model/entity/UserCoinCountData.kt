package org.liupack.wanandroid.model.entity


import androidx.compose.runtime.Stable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class UserCoinCountData(
    @SerialName("coinCount")
    val coinCount: Int,
    @SerialName("rank")
    val rank: Int,
    @SerialName("userId")
    val userId: Int,
    @SerialName("username")
    val username: String
) {
    companion object {
        val Empty = UserCoinCountData(0, 0, 0, "")
    }
}