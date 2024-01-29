package org.liupack.wanandroid.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.BackStackEntry
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.RouteBuilder
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.liupack.wanandroid.common.rememberCountDownTimer
import org.liupack.wanandroid.composables.rememberLogger
import org.liupack.wanandroid.router.Router

fun RouteBuilder.splashScreen(navigator: Navigator) {
    scene(Router.Splash.path) {
        SplashScreen(navigator = navigator, backStackEntry = it)
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SplashScreen(navigator: Navigator, backStackEntry: BackStackEntry) {
    val logger = rememberLogger()
    val countDownTimer = rememberCountDownTimer(5)
    val countDown by countDownTimer.currentTimer.collectAsStateWithLifecycle()
    val navigatorHome = {
        navigator.navigate(
            Router.Home.path,
            NavOptions(popUpTo = PopUpTo.First(inclusive = true), launchSingleTop = true)
        )
    }
    DisposableEffect(backStackEntry.active()) {
        countDownTimer.start()
        onDispose {
            countDownTimer.stop()
        }
    }
    if (countDown == 0) {
        navigatorHome.invoke()
    } else {
        logger.i("countDown hashCode is $countDown")
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    Image(
                        painter = painterResource("icon.png"),
                        contentDescription = null,
                        modifier = Modifier.size(100.dp),
                    )
                    Text("Wan Android App")
                }
            )
            OutlinedButton(modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(12.dp).align(Alignment.TopEnd), onClick = {
                countDownTimer.stop()
                navigatorHome.invoke()
            }, content = {
                Text("跳过${countDown}")
            })
        }
    }
}