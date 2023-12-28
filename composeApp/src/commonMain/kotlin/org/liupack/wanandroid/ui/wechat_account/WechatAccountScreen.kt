package org.liupack.wanandroid.ui.wechat_account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LibraryBooks
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

@Stable
object WechatAccountScreen : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Outlined.LibraryBooks)
            return remember {
                TabOptions(index = 2u, title = "公众号", icon = icon)
            }
        }

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<WechatAccountViewModel>()
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("公众号${viewModel.hashCode()}")
        }
    }
}