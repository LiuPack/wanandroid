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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.RouteBuilder
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.liupack.wanandroid.common.rememberCountDownTimer
import org.liupack.wanandroid.router.Router

fun RouteBuilder.splashScreen(navigator: Navigator) {
    scene(Router.Splash.path) {
        val countDownTimer = rememberCountDownTimer(5)
        val countDown by countDownTimer.currentTimer.collectAsStateWithLifecycle()
        val navigatorHome = {
            navigator.navigate(
                Router.Home.path,
                NavOptions(popUpTo = PopUpTo.First(inclusive = true), launchSingleTop = true)
            )
        }
        DisposableEffect(countDownTimer) {
            countDownTimer.start()
            onDispose {
                countDownTimer.stop()
            }
        }
        if (countDown == 0) {
            navigatorHome.invoke()
        } else {
            println("countDown hashCode is $countDown")
        }
        SplashScreen(countDown = countDown, skipClick = {
            countDownTimer.stop()
            navigatorHome.invoke()
        })
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SplashScreen(countDown: Int, skipClick: () -> Unit = {}) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            content = {
                Image(
                    painter = painterResource("compose-multiplatform.xml"),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
                Text("PreCompose App")
            }
        )
        OutlinedButton(modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(12.dp).align(Alignment.TopEnd), onClick = skipClick, content = {
            Text("跳过${countDown}")
        })
    }
}