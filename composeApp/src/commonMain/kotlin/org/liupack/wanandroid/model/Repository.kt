package org.liupack.wanandroid.model

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.liupack.wanandroid.model.entity.BannerData
import org.liupack.wanandroid.model.entity.CoinCountRankingData
import org.liupack.wanandroid.model.entity.HomeArticleItemData
import org.liupack.wanandroid.model.entity.NullData
import org.liupack.wanandroid.model.entity.ProjectSortData
import org.liupack.wanandroid.model.entity.SystemBaseData
import org.liupack.wanandroid.model.entity.UserCoinCountData
import org.liupack.wanandroid.model.entity.UserCoinCountListData
import org.liupack.wanandroid.model.entity.UserFullInfoData
import org.liupack.wanandroid.model.entity.UserInfoData

interface Repository {

    /**
     * 登录账号
     *
     * @param userName 帐号名
     * @param password 账号密码
     * @return
     */
    fun login(userName: String, password: String): Flow<UserInfoData?>

    /**
     * 退出登录
     *
     * @return
     */
    fun logout(): Flow<NullData?>

    /**
     * 注册账号
     *
     * @param userName 帐号名
     * @param password 账号密码
     * @param rePassword 确认账号密码
     * @return
     */
    fun register(userName: String, password: String, rePassword: String): Flow<UserInfoData?>

    /**
     * 用户基本信息
     *
     * @return
     */
    fun userInfo(): Flow<UserFullInfoData?>

    /**
     * 轮播图
     *
     * @return
     */
    fun bannerList(): Flow<List<BannerData>>

    /**
     * 文章列表
     *
     * @return
     */
    fun articles(): Flow<PagingData<HomeArticleItemData>>

    /**
     * 项目分类
     *
     * @return
     */
    fun projectSort(): Flow<List<ProjectSortData>>

    /**
     * 分类下的项目数据
     *
     * @param id 分类 id
     * @return
     */
    fun projectListFromSort(id: Int): Flow<PagingData<HomeArticleItemData>>

    /**
     * 体系分类数据
     *
     * @return
     */
    fun systemBaseList(): Flow<List<SystemBaseData>>

    /**
     * 用户总积分
     *
     * @return
     */
    fun userCoinCount(): Flow<UserCoinCountData>

    /**
     * 用户积分记录
     *
     * @return
     */
    fun userCoinCountList(): Flow<PagingData<UserCoinCountListData>>

    /**
     * 积分排行榜
     *
     * @return
     */
    fun coinCountRanking(): Flow<PagingData<CoinCountRankingData>>

    fun articleInSystem(cid: Int): Flow<PagingData<HomeArticleItemData>>
}