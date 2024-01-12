package org.liupack.wanandroid.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.liupack.wanandroid.common.Logger

@Composable
fun rememberLogger(): Logger {
    return remember { Logger }
}