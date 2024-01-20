package org.liupack.wanandroid.model.datasource

import androidx.paging.PagingState
import app.cash.paging.PagingSource
import io.ktor.client.request.get
import org.liupack.wanandroid.model.entity.HomeArticleItemData
import org.liupack.wanandroid.model.entity.UserFavoriteArticleData
import org.liupack.wanandroid.network.DataResult.Companion.catchData
import org.liupack.wanandroid.network.NetworkConfig
import org.liupack.wanandroid.network.NetworkConfig.replaceRealPageApi
import org.liupack.wanandroid.network.PagingListData
import org.liupack.wanandroid.network.connect
import org.liupack.wanandroid.network.dataResultBody

class UserFavoriteArticlesSource : PagingSource<Int, UserFavoriteArticleData>() {

    override fun getRefreshKey(state: PagingState<Int, UserFavoriteArticleData>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserFavoriteArticleData> {
        return runCatching {
            val page = params.key ?: 0
            val result = connect().get(NetworkConfig.userFavoriteArticles.replaceRealPageApi(page))
                .dataResultBody<PagingListData<UserFavoriteArticleData>>().catchData
            val dataList = result?.dataList.orEmpty()
            LoadResult.Page(
                data = dataList,
                prevKey = null,
                nextKey = if (dataList.size < params.loadSize) null else page + 1
            )
        }.getOrElse { LoadResult.Error(it) }
    }
}