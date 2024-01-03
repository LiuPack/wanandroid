package org.liupack.wanandroid

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import org.koin.compose.KoinApplication
import org.liupack.wanandroid.model.di.appModule
import org.liupack.wanandroid.theme.AppTheme
import org.liupack.wanandroid.ui.main.MainScreen

@Composable
internal fun App() {
    KoinApplication(application = {
        modules(appModule)
    }, content = {
        AppTheme {
            Navigator(screen = MainScreen)
        }
    })
}

internal expect fun openUrl(url: String?)