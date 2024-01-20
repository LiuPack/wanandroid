package org.liupack.wanandroid.model.entity

import androidx.compose.runtime.Stable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class CoinInfoData(
    @SerialName("coinCount")
    val coinCount: Int,
    @SerialName("level")
    val level: Int,
    @SerialName("nickname")
    val nickName: String,
    @SerialName("rank")
    val rank: String,
    @SerialName("userId")
    val userId: Int,
    @SerialName("username")
    val userName: String,
)
