package org.liupack.wanandroid.network

import androidx.compose.runtime.Stable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Stable
@Serializable
data class PagingListData<out T>(
    @SerialName("curPage")
    val curPage: Int,
    @SerialName("datas")
    val dataList: List<T>,
    @SerialName("pageCount")
    val pageCount: Int,
    @SerialName("size")
    val size: Int,
    @SerialName("total")
    val total: Int,
)
