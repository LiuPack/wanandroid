package org.liupack.wanandroid.ui.user_coincount

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import org.liupack.wanandroid.composables.IconBackButton
import org.liupack.wanandroid.ui.home.LocalNavigatorParent

data object UserCoinCountScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigatorParent
        val viewModel = getScreenModel<UserCoinCountViewModel>()
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            TopAppBar(
                title = { Text("我的积分") },
                navigationIcon = { IconBackButton(onClick = { navigator.pop() }) })
        }, content = {})
    }
}