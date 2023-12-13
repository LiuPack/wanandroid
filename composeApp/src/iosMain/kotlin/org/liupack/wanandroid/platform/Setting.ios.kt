package org.liupack.wanandroid.platform

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import platform.Foundation.NSUserDefaults

actual fun createSetting(): Settings {
    val delegate = NSUserDefaults.standardUserDefaults()
    return NSUserDefaultsSettings(delegate)
}