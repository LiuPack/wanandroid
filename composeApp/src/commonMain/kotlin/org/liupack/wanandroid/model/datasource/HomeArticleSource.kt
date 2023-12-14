package org.liupack.wanandroid.model.datasource

import androidx.paging.PagingState
import app.cash.paging.PagingSource
import io.ktor.client.request.get
import org.liupack.wanandroid.model.entity.HomeArticleItemData
import org.liupack.wanandroid.network.DataResult.Companion.catchData
import org.liupack.wanandroid.network.PagingListData
import org.liupack.wanandroid.network.connect
import org.liupack.wanandroid.network.dataResultBody

class HomeArticleSource() : PagingSource<Int, HomeArticleItemData>() {

    override fun getRefreshKey(state: PagingState<Int, HomeArticleItemData>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HomeArticleItemData> {
        return runCatching {
            val page = params.key ?: 0
            val result = connect().get("article/list/${page}/json")
                .dataResultBody<PagingListData<HomeArticleItemData>>().catchData
            LoadResult.Page(
                data = result?.dataList.orEmpty(),
                prevKey = null,
                nextKey = if (result?.dataList.isNullOrEmpty()) null else result?.curPage
            )
        }.getOrElse { LoadResult.Error(it) }
    }
}