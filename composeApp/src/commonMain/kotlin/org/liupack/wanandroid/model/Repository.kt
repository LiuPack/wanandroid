package org.liupack.wanandroid.model

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.liupack.wanandroid.model.entity.BannerData
import org.liupack.wanandroid.model.entity.HomeArticleItemData
import org.liupack.wanandroid.model.entity.ProjectSortData
import org.liupack.wanandroid.model.entity.UserFullInfoData
import org.liupack.wanandroid.model.entity.UserInfoData

interface Repository {

    fun login(userName: String, password: String): Flow<UserInfoData?>

    fun register(userName: String, password: String, rePassword: String): Flow<UserInfoData?>

    fun userInfo(): Flow<UserFullInfoData?>

    fun bannerList(): Flow<List<BannerData>>

    fun articles(): Flow<PagingData<HomeArticleItemData>>

    fun projectSort(): Flow<List<ProjectSortData>>

    fun projectListFromSort(id: Int): Flow<PagingData<HomeArticleItemData>>
}