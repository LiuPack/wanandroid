package org.liupack.wanandroid.model.datasource

import androidx.paging.PagingState
import app.cash.paging.PagingSource
import io.ktor.client.request.get
import org.liupack.wanandroid.model.entity.HomeArticleItemData
import org.liupack.wanandroid.model.entity.UserSharedData
import org.liupack.wanandroid.network.DataResult.Companion.catchData
import org.liupack.wanandroid.network.NetworkConfig
import org.liupack.wanandroid.network.NetworkConfig.replaceRealPageApi
import org.liupack.wanandroid.network.connect
import org.liupack.wanandroid.network.dataResultBody

class UserShareArticlesSource : PagingSource<Int, HomeArticleItemData>() {

    override fun getRefreshKey(state: PagingState<Int, HomeArticleItemData>): Int? {
        return state.anchorPosition?.let {
            val anchorPosition = state.closestPageToPosition(it)
            return anchorPosition?.prevKey?.plus(1) ?: anchorPosition?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HomeArticleItemData> {
        return runCatching {
            val page = params.key ?: 1
            val result = connect().get(NetworkConfig.userShareArticles.replaceRealPageApi(page))
                .dataResultBody<UserSharedData>().catchData
            val dataList = result?.shareArticles?.dataList.orEmpty()
            LoadResult.Page(
                data = dataList,
                prevKey = null,
                nextKey = if (dataList.size < params.loadSize) null else page + 1
            )
        }.getOrElse { LoadResult.Error(it) }
    }
}