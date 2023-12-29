package org.liupack.wanandroid.model.entity

import androidx.compose.runtime.Stable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class UserFullInfoData(
    @SerialName("coinInfo")
    val coinInfoData: CoinInfoData?,
    @SerialName("userInfo")
    val userInfoData: UserInfoData?,
)
