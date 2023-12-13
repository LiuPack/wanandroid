package org.liupack.wanandroid.platform

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.liupack.wanandroid.AndroidApp

actual fun createSetting(): Settings {
    val delegate = AndroidApp.INSTANCE.getSharedPreferences("cache", Context.MODE_PRIVATE)
    return SharedPreferencesSettings(delegate)
}