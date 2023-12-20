package org.liupack.wanandroid.model

import androidx.paging.PagingConfig
import app.cash.paging.Pager
import app.cash.paging.PagingData
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.http.parameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.liupack.wanandroid.model.datasource.HomeArticleSource
import org.liupack.wanandroid.model.datasource.ProjectListSource
import org.liupack.wanandroid.model.entity.BannerData
import org.liupack.wanandroid.model.entity.HomeArticleItemData
import org.liupack.wanandroid.model.entity.ProjectSortData
import org.liupack.wanandroid.model.entity.UserFullInfoData
import org.liupack.wanandroid.model.entity.UserInfoData
import org.liupack.wanandroid.network.DataResult.Companion.catchData
import org.liupack.wanandroid.network.NetworkConfig
import org.liupack.wanandroid.network.connect
import org.liupack.wanandroid.network.dataResultBody

class DefaultRepository : Repository {
    override fun login(userName: String, password: String): Flow<UserInfoData?> {
        return flow {
            val result = connect().submitForm(NetworkConfig.loginApi, parameters {
                append("username", userName)
                append("password", password)
            }).dataResultBody<UserInfoData>().catchData
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    override fun register(
        userName: String,
        password: String,
        rePassword: String
    ): Flow<UserInfoData?> {
        return flow {
            val result = connect().submitForm(NetworkConfig.registerApi, parameters {
                append("username", userName)
                append("password", password)
                append("repassword", rePassword)
            }).dataResultBody<UserInfoData>().catchData
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    override fun userInfo(): Flow<UserFullInfoData?> {
        return flow {
            val result =
                connect().get(NetworkConfig.userInfo).dataResultBody<UserFullInfoData>().catchData
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    override fun bannerList(): Flow<List<BannerData>> {
        return flow {
            val result = connect().get(NetworkConfig.bannerApi)
                .dataResultBody<List<BannerData>>().catchData.orEmpty()
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    override fun articles(): Flow<PagingData<HomeArticleItemData>> {
        return Pager(
            config = PagingConfig(initialLoadSize = 10, pageSize = 20, prefetchDistance = 1),
            pagingSourceFactory = { HomeArticleSource() }
        ).flow.flowOn(Dispatchers.IO)
    }

    override fun projectSort(): Flow<List<ProjectSortData>> {
        return flow {
            val result = connect().get(NetworkConfig.projectSortApi)
                .dataResultBody<List<ProjectSortData>>().catchData.orEmpty()
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    override fun projectListFromSort(id: Int): Flow<PagingData<HomeArticleItemData>> {
        return Pager(
            config = PagingConfig(initialLoadSize = 10, pageSize = 20, prefetchDistance = 1),
            pagingSourceFactory = { ProjectListSource(id) }
        ).flow.flowOn(Dispatchers.IO)
    }
}