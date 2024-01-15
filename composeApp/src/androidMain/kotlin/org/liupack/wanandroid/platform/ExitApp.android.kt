package org.liupack.wanandroid.platform

import org.liupack.wanandroid.ActivityManager

actual fun exitApp() {
    ActivityManager.activity.finish()
}