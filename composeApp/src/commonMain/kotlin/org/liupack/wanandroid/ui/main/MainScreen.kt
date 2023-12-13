package org.liupack.wanandroid.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.rememberImagePainter
import kotlinx.coroutines.launch
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.rememberNavigator
import org.liupack.wanandroid.model.entity.BottomNavigatorData
import org.liupack.wanandroid.router.Router
import org.liupack.wanandroid.ui.home.HomeScreen
import org.liupack.wanandroid.ui.login.LoginScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navigator = rememberNavigator()
    val currentBackStackEntry by navigator.currentEntry.collectAsStateWithLifecycle(null)
    val viewModel = koinViewModel(MainViewModel::class)
    val scope = rememberCoroutineScope()
    val isLogin by remember(viewModel.isLogin) { viewModel.isLogin }
    var inMain by rememberSaveable(viewModel) { mutableStateOf(true) }
    LaunchedEffect(currentBackStackEntry) {
        inMain = currentBackStackEntry?.path in viewModel.mainRoute
    }
    val selected by viewModel.currentNavigator.collectAsStateWithLifecycle(BottomNavigatorData.defaultList.first())
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AnimatedVisibility(inMain, enter = expandVertically(), exit = shrinkVertically()) {
                TopAppBar(
                    title = {
                        if (!isLogin) {
                            Button(onClick = {
                                scope.launch {
                                    navigator.navigateForResult(
                                        route = Router.Login.path,
                                        options = NavOptions(launchSingleTop = true)
                                    )
                                }
                            }, content = { Text("未登录，点击登录") })
                        }
                    },
                    navigationIcon = {
                        if (isLogin) {
                            Image(
                                painter = rememberImagePainter(""),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp).clip(CircleShape)
                            )
                        }
                    },
                )
            }
        },
        bottomBar = {
            AnimatedVisibility(inMain, enter = expandVertically(), exit = shrinkVertically()) {
                NavigationBar(modifier = Modifier.fillMaxWidth()) {
                    viewModel.navigators.forEach {
                        NavigationBarItem(
                            selected = selected == it,
                            icon = {
                                Image(imageVector = it.icon, contentDescription = null)
                            }, label = {
                                Text(text = it.label)
                            }, onClick = {
                                viewModel.updateSelectedNavigator(it)
                            })
                    }
                }
            }
        },
        content = {
            NavHost(
                modifier = Modifier.fillMaxSize(),
                navigator = navigator,
                initialRoute = Router.Home.path,
                builder = {
                    scene(Router.Login.path) {
                        LoginScreen(navigator)
                    }
                    scene(Router.Home.path) {
                        HomeScreen()
                    }
                })
        })
}