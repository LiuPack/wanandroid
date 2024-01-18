package org.liupack.wanandroid.ui.setting

import com.russhwolf.settings.contains
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.liupack.wanandroid.common.Constants
import org.liupack.wanandroid.platform.settings
import org.liupack.wanandroid.theme.ThemeMode

class SettingViewModel : ViewModel() {

    fun saveSystemThemeMode(themeMode: ThemeMode) {
        viewModelScope.launch {
            settings.putBoolean(
                Constants.isAutoDarkMode,
                themeMode is ThemeMode.System
            )
            if (settings.contains(Constants.darkTheme)) {
                settings.remove(Constants.darkTheme)
            }
        }
    }

    fun saveDarkTheme(themeMode: ThemeMode) {
        viewModelScope.launch {
            settings.putBoolean(
                Constants.darkTheme,
                themeMode is ThemeMode.Dark
            )
        }
    }
}
