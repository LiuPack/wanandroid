package org.liupack.wanandroid.platform

import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import java.util.prefs.Preferences

actual fun createSetting(): Settings {
    val delegate = Preferences.userRoot()
    return PreferencesSettings(delegate)
}