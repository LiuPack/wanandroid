package org.liupack.wanandroid.platform

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSSelectorFromString
import platform.UIKit.UIApplication

@OptIn(ExperimentalForeignApi::class)
actual fun exitApp() {
    UIApplication.sharedApplication.performSelector(
        aSelector = NSSelectorFromString("terminateWithSuccess"),
        withObject = null
    )
}