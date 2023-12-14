package org.liupack.wanandroid.model.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserFullInfoData(
    @SerialName("coinInfo")
    val coinInfoData: CoinInfoData?,
    @SerialName("userInfo")
    val userInfoData: UserInfoData?,
)
