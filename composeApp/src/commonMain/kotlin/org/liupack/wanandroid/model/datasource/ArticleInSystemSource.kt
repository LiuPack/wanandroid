package org.liupack.wanandroid.model.datasource

import androidx.paging.PagingState
import app.cash.paging.PagingSource
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.liupack.wanandroid.model.entity.HomeArticleItemData
import org.liupack.wanandroid.network.DataResult.Companion.catchData
import org.liupack.wanandroid.network.NetworkConfig
import org.liupack.wanandroid.network.NetworkConfig.replaceRealPageApi
import org.liupack.wanandroid.network.PagingListData
import org.liupack.wanandroid.network.connect
import org.liupack.wanandroid.network.dataResultBody

class ArticleInSystemSource(private val cid: Int) : PagingSource<Int, HomeArticleItemData>() {

    override fun getRefreshKey(state: PagingState<Int, HomeArticleItemData>): Int? {
        return state.anchorPosition?.let {
            val anchorPosition = state.closestPageToPosition(it)
            return anchorPosition?.prevKey?.plus(1) ?: anchorPosition?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HomeArticleItemData> {
        return runCatching {
            val page = params.key ?: 0
            val result = connect().get(NetworkConfig.articleInSystemApi.replaceRealPageApi(page)) {
                parameter("cid", cid)
            }.dataResultBody<PagingListData<HomeArticleItemData>>().catchData
            val dataList = result?.dataList.orEmpty()
            LoadResult.Page(
                data = dataList,
                prevKey = null,
                nextKey = if (dataList.size < params.loadSize) null else page + 1
            )
        }.getOrElse { LoadResult.Error(it) }
    }
}