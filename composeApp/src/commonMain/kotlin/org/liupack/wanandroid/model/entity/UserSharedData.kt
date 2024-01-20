package org.liupack.wanandroid.model.entity

import androidx.compose.runtime.Stable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.liupack.wanandroid.network.PagingListData

@Stable
@Serializable
data class UserSharedData(
    @SerialName("coinInfo")
    val coinInfoData: CoinInfoData?,
    @SerialName("shareArticles")
    val shareArticles: PagingListData<HomeArticleItemData>
)
