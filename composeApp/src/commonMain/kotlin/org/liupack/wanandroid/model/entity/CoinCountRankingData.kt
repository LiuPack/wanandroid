package org.liupack.wanandroid.model.entity


import androidx.compose.runtime.Stable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class CoinCountRankingData(
    @SerialName("coinCount")
    val coinCount: Int,
    @SerialName("level")
    val level: Int,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("rank")
    val rank: String,
    @SerialName("userId")
    val userId: Int,
    @SerialName("username")
    val username: String
) {
    val rankDesc
        get() = when (rank) {
            "1" -> "\uD83E\uDD47"
            "2" -> "\uD83E\uDD48"
            "3" -> "\uD83E\uDD49"
            else -> rank
        }
}