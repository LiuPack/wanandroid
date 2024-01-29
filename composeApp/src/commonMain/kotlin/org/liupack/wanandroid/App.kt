package org.liupack.wanandroid

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import moe.tlaster.precompose.PreComposeApp
import org.koin.compose.KoinContext
import org.liupack.wanandroid.theme.AppTheme
import org.liupack.wanandroid.ui.main.MainScreen

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun App() {
    PreComposeApp {
        KoinContext {
            AppTheme {
                val windowSizeClass = calculateWindowSizeClass()
                MainScreen(windowSizeClass = windowSizeClass)
            }
        }
    }
}

internal expect fun openUrl(url: String?)