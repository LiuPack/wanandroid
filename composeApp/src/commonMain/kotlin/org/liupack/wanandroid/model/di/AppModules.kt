package org.liupack.wanandroid.model.di

import org.koin.dsl.module
import org.liupack.wanandroid.model.DefaultRepository
import org.liupack.wanandroid.model.Repository
import org.liupack.wanandroid.ui.home.HomeViewModel
import org.liupack.wanandroid.ui.login.LoginViewModel
import org.liupack.wanandroid.ui.main.MainViewModel
import org.liupack.wanandroid.ui.project.ProjectListViewModel
import org.liupack.wanandroid.ui.project.ProjectViewModel
import org.liupack.wanandroid.ui.register.RegisterViewModel
import org.liupack.wanandroid.ui.system.SystemViewModel
import org.liupack.wanandroid.ui.user.UserViewModel
import org.liupack.wanandroid.ui.wechat_account.WechatAccountViewModel

val appModule = module {
    single<Repository> { DefaultRepository() }
    factory { LoginViewModel(get()) }
    factory { MainViewModel(get()) }
    factory { RegisterViewModel(get()) }
    factory { HomeViewModel(get()) }
    factory { SystemViewModel(get()) }
    factory { WechatAccountViewModel(get()) }
    factory { ProjectViewModel(get()) }
    factory { UserViewModel(get()) }
    factory { ProjectListViewModel(get()) }
}