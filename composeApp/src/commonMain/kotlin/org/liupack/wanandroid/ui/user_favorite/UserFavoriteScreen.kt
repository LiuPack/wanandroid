package org.liupack.wanandroid.ui.user_favorite

import androidx.compose.runtime.Composable
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import org.liupack.wanandroid.router.Router

fun RouteBuilder.userFavoriteScreen(navigator: Navigator) {
    scene(Router.UserFavorite.path) {
        UserFavoriteScreen(navigator)
    }
}

@Composable
fun UserFavoriteScreen(navigator: Navigator) {
    val viewModel = koinViewModel(UserFavoriteViewModel::class)
}
