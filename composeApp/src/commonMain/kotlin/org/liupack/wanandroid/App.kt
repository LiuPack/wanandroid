package org.liupack.wanandroid

import androidx.compose.runtime.Composable
import moe.tlaster.precompose.PreComposeApp
import org.koin.compose.KoinApplication
import org.liupack.wanandroid.model.di.appModule
import org.liupack.wanandroid.theme.AppTheme
import org.liupack.wanandroid.ui.main.MainScreen

@Composable
internal fun App() {
    PreComposeApp {
        KoinApplication(application = {
            modules(appModule)
        }, content = {
            AppTheme {
                MainScreen()
            }
        })
    }
}

internal expect fun openUrl(url: String?)