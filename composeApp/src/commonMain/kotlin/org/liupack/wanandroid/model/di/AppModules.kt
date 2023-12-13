package org.liupack.wanandroid.model.di

import org.koin.dsl.module
import org.liupack.wanandroid.model.DefaultRepository
import org.liupack.wanandroid.model.Repository
import org.liupack.wanandroid.ui.login.LoginViewModel
import org.liupack.wanandroid.ui.main.MainViewModel
import org.liupack.wanandroid.ui.register.RegisterViewModel

val appModule = module {
    single<Repository> { DefaultRepository() }
    factory { LoginViewModel(get()) }
    factory { MainViewModel(get()) }
    factory { RegisterViewModel(get()) }
}