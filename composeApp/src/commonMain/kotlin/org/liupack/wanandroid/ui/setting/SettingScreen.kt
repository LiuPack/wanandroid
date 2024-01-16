package org.liupack.wanandroid.ui.setting

import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import org.liupack.wanandroid.common.Constants
import org.liupack.wanandroid.composables.IconBackButton
import org.liupack.wanandroid.platform.settings
import org.liupack.wanandroid.router.Router
import org.liupack.wanandroid.theme.LocalThemeIsDark

fun RouteBuilder.settingScreen(navigator: Navigator) {
    scene(Router.Setting.path) {
        SettingScreen(backClick = { navigator.goBack() })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(backClick: () -> Unit = {}) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("设置") },
                navigationIcon = {
                    IconBackButton(onClick = backClick)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding()),
            ) {
                item {
                    ListItem(
                        modifier = Modifier.fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface), headlineContent = {
                            Text("暗色模式")
                        }, trailingContent = {
                            var darkTheme by LocalThemeIsDark.current
                            Switch(checked = darkTheme, onCheckedChange = {
                                darkTheme = !darkTheme
                                settings.putBoolean(Constants.darkTheme, darkTheme)
                            })
                        })
                }
            }
        },
    )
}