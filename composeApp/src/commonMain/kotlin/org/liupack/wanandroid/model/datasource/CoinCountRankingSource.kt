package org.liupack.wanandroid.model.datasource

import app.cash.paging.PagingSource
import app.cash.paging.PagingState
import io.ktor.client.request.get
import org.liupack.wanandroid.model.entity.CoinCountRankingData
import org.liupack.wanandroid.network.DataResult.Companion.catchData
import org.liupack.wanandroid.network.NetworkConfig
import org.liupack.wanandroid.network.NetworkConfig.replaceRealPageApi
import org.liupack.wanandroid.network.PagingListData
import org.liupack.wanandroid.network.connect
import org.liupack.wanandroid.network.dataResultBody

class CoinCountRankingSource : PagingSource<Int, CoinCountRankingData>() {

    override fun getRefreshKey(state: PagingState<Int, CoinCountRankingData>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CoinCountRankingData> {
        return runCatching {
            val page = params.key ?: 1
            val result = connect().get(NetworkConfig.coinCountRankingApi.replaceRealPageApi(page))
                .dataResultBody<PagingListData<CoinCountRankingData>>().catchData
            val dataList = result?.dataList.orEmpty()
            LoadResult.Page(
                data = dataList,
                prevKey = null,
                nextKey = if (dataList.size < params.loadSize) null else page + 1
            )
        }.getOrElse { LoadResult.Error(it) }
    }
}