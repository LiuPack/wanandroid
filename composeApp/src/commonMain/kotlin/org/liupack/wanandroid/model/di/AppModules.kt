package org.liupack.wanandroid.model.di

import org.koin.dsl.module
import org.liupack.wanandroid.model.DefaultRepository
import org.liupack.wanandroid.model.Repository
import org.liupack.wanandroid.model.usecase.CancelFavoriteArticleUseCase
import org.liupack.wanandroid.model.usecase.CancelUserFavoriteArticleUseCase
import org.liupack.wanandroid.model.usecase.FavoriteArticleUseCase
import org.liupack.wanandroid.ui.coin_count_ranking.CoinCountRankingViewModel
import org.liupack.wanandroid.ui.home.HomeViewModel
import org.liupack.wanandroid.ui.login.LoginViewModel
import org.liupack.wanandroid.ui.main.MainViewModel
import org.liupack.wanandroid.ui.open_source.OpenSourceViewModel
import org.liupack.wanandroid.ui.project.ProjectViewModel
import org.liupack.wanandroid.ui.project.child.ProjectListViewModel
import org.liupack.wanandroid.ui.register.RegisterViewModel
import org.liupack.wanandroid.ui.setting.SettingViewModel
import org.liupack.wanandroid.ui.system.SystemViewModel
import org.liupack.wanandroid.ui.system.articles_in_system.ArticleInSystemViewModel
import org.liupack.wanandroid.ui.user.UserViewModel
import org.liupack.wanandroid.ui.user_coincount.UserCoinCountViewModel
import org.liupack.wanandroid.ui.user_favorite.UserFavoriteViewModel
import org.liupack.wanandroid.ui.user_shared.UserSharedViewModel
import org.liupack.wanandroid.ui.user_shared.add_shared.UserAddSharedViewModel
import org.liupack.wanandroid.ui.wechat_account.WechatAccountViewModel
import org.liupack.wanandroid.ui.wechat_account.child.ArticleInWechatAccountViewModel

val appModule = module {
    single<Repository> { DefaultRepository() }
    single { FavoriteArticleUseCase(get()) }
    single { CancelFavoriteArticleUseCase(get()) }
    single { CancelUserFavoriteArticleUseCase(get()) }
    factory { LoginViewModel(get()) }
    factory { MainViewModel() }
    factory { RegisterViewModel(get()) }
    factory { HomeViewModel(get(), get(), get()) }
    factory { SystemViewModel(get()) }
    factory { WechatAccountViewModel(get()) }
    factory { ProjectViewModel(get()) }
    factory { UserViewModel(get()) }
    factory { (id: Int) -> ProjectListViewModel(id, get(), get(), get()) }
    factory { UserCoinCountViewModel(get()) }
    factory { CoinCountRankingViewModel(get()) }
    factory { (cid: Int) -> ArticleInSystemViewModel(cid, get(), get(), get()) }
    factory { SettingViewModel() }
    factory { WechatAccountViewModel(get()) }
    factory { (id: Int) -> ArticleInWechatAccountViewModel(id, get(), get(), get()) }
    factory { UserFavoriteViewModel(get(), get()) }
    factory { UserSharedViewModel(get()) }
    factory { UserAddSharedViewModel(get()) }
    factory { OpenSourceViewModel() }
}