import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.datlag.kcef.KCEF
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.painterResource
import org.koin.core.context.startKoin
import org.liupack.wanandroid.App
import org.liupack.wanandroid.model.di.appModule
import java.awt.Dimension
import kotlin.math.max

fun main() = application {
    startKoin {
        modules(appModule)
    }
    Window(
        title = "玩 Android",
        icon = painterResource("android_studio.png"),
        state = rememberWindowState(width = 350.dp, height = 700.dp),
        onCloseRequest = ::exitApplication,
    ) {
        window.minimumSize = Dimension(350, 700)
        var restartRequired by remember { mutableStateOf(false) }
        var downloading by remember { mutableStateOf(0f) }
        var initialized by remember { mutableStateOf(false) }
        var failedMsg by remember { mutableStateOf("") }
        LaunchedEffect(Unit) {
            withContext(Dispatchers.IO) {
                KCEF.init(builder = {
                    progress {
                        onDownloading {
                            downloading = max(it, 0f)
                        }
                        onInitialized {
                            initialized = true
                        }
                        release(true)
                    }
                }, onError = {
                    failedMsg = it?.stackTraceToString().orEmpty()
                    it?.printStackTrace()
                }, onRestartRequired = {
                    restartRequired = true
                })
            }
        }
        if (restartRequired) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center, content = {
                Text(text = failedMsg)
            })
        } else {
            if (initialized) {
                App()
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                    content = {
                        Text(text = "Downloading $downloading%")
                    })
            }
        }
        DisposableEffect(Unit) {
            onDispose {
                KCEF.disposeBlocking()
            }
        }
    }
}