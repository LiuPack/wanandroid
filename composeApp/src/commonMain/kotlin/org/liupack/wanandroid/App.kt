package org.liupack.wanandroid

import androidx.compose.runtime.Composable
import moe.tlaster.precompose.PreComposeApp
import org.koin.compose.KoinContext
import org.liupack.wanandroid.ui.main.MainScreen
import theme.AppTheme

@Composable
internal fun App() {
    PreComposeApp {
        KoinContext {
            AppTheme {
                MainScreen()
            }
        }
    }
}

internal expect fun openUrl(url: String?)