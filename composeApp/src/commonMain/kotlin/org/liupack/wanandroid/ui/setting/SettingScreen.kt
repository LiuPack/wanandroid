package org.liupack.wanandroid.ui.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import org.liupack.wanandroid.common.Constants
import org.liupack.wanandroid.composables.IconBackButton
import org.liupack.wanandroid.platform.settings
import org.liupack.wanandroid.router.Router
import org.liupack.wanandroid.theme.LocalShowPinned
import org.liupack.wanandroid.theme.LocalThemeMode
import org.liupack.wanandroid.theme.ThemeMode

fun RouteBuilder.settingScreen(navigator: Navigator) {
    scene(Router.Setting.path) {
        val viewModel = koinViewModel(SettingViewModel::class)
        SettingScreen(
            backClick = { navigator.goBack() },
            onSystemChange = { viewModel.saveSystemThemeMode(it) },
            onDarkChange = { viewModel.saveDarkTheme(it) },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    backClick: () -> Unit = {},
    onSystemChange: (ThemeMode) -> Unit,
    onDarkChange: (ThemeMode) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("设置") },
                navigationIcon = {
                    IconBackButton(onClick = backClick)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = contentColorFor(MaterialTheme.colorScheme.background),
                    actionIconContentColor = contentColorFor(MaterialTheme.colorScheme.background),
                    navigationIconContentColor = contentColorFor(MaterialTheme.colorScheme.background),
                ),
            )
        },
        content = { paddingValues ->
            SettingContent(
                paddingValues = paddingValues,
                onSystemChange = onSystemChange,
                onDarkChange = onDarkChange
            )
        },
    )
}

@Composable
private fun SettingContent(
    paddingValues: PaddingValues,
    onSystemChange: (ThemeMode) -> Unit,
    onDarkChange: (ThemeMode) -> Unit
) {
    var themeMode by LocalThemeMode.current
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(top = paddingValues.calculateTopPadding()),
    ) {
        item {
            ListItem(modifier = Modifier.fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface),
                headlineContent = {
                    Text("跟随系统")
                },
                trailingContent = {
                    Switch(checked = themeMode is ThemeMode.System, onCheckedChange = {
                        themeMode = if (it) {
                            ThemeMode.System
                        } else {
                            if (!settings.getBoolean(Constants.darkTheme, false)) {
                                ThemeMode.Light
                            } else {
                                ThemeMode.Dark
                            }
                        }
                        onSystemChange.invoke(themeMode)
                    })
                })
        }
        item {
            ListItem(modifier = Modifier.fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface),
                headlineContent = {
                    Text("暗色模式")
                },
                trailingContent = {
                    Switch(checked = themeMode is ThemeMode.Dark, onCheckedChange = {
                        themeMode = if (it) {
                            ThemeMode.Dark
                        } else {
                            ThemeMode.Light
                        }
                        onDarkChange.invoke(themeMode)
                    }, enabled = themeMode !is ThemeMode.System)
                })
        }
        item {
            var showPinned by LocalShowPinned.current
            ListItem(modifier = Modifier.fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface),
                headlineContent = {
                    Text("显示置顶文章")
                },
                trailingContent = {
                    Switch(checked = showPinned, onCheckedChange = {
                        showPinned = !showPinned
                        settings.putBoolean(Constants.showPinned, showPinned)
                    })
                })
        }
    }
}