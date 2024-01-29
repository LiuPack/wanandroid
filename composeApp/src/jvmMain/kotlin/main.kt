import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.datlag.kcef.KCEF
import dev.datlag.tooling.Tooling
import dev.datlag.tooling.getApplicationWriteableRootFolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.context.startKoin
import org.liupack.wanandroid.App
import org.liupack.wanandroid.model.di.appModule
import wanandroid.composeApp.BuildConfig
import java.awt.Dimension
import java.io.File
import java.math.RoundingMode
import kotlin.math.max

fun main() = application {
    startKoin {
        modules(appModule)
    }
    Window(
        title = "玩 Android",
        icon = painterResource("icon.png"),
        onCloseRequest = ::exitApplication,
    ) {
        window.maximumSize = Dimension(840, window.height)
        window.minimumSize = Dimension(350, 700)
        var restartRequired by remember { mutableStateOf(false) }
        var downloading by remember { mutableStateOf(0f) }
        var initialized by remember { mutableStateOf(false) }
        var failedMsg by remember { mutableStateOf("") }
        var rootPath by remember { mutableStateOf("") }
        val isDebug = !BuildConfig.DEBUG
        LaunchedEffect(Unit) {
            withContext(Dispatchers.IO) {
                //https://github.com/DatL4g/KCEF/issues/2
                val kcefInstallDir = if (isDebug) {
                    File("kcef-bundle")
                } else {
                    val rootFolder =
                        Tooling.getApplicationWriteableRootFolder("wanandroid") ?: File("./")
                    File(rootFolder, "kcef-bundle")
                }
                rootPath = kcefInstallDir.absolutePath
                KCEF.init(builder = {
                    installDir(kcefInstallDir)
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
                        Text(
                            buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("配置路径:")
                                }
                                withStyle(SpanStyle()) {
                                    append(rootPath)
                                }
                                withStyle(ParagraphStyle()) {
                                    append("模式：")
                                    if (isDebug) {
                                        append("Debug")
                                    } else {
                                        append("Release")
                                    }
                                }
                            },
                            modifier = Modifier.padding(12.dp).fillMaxWidth()
                                .align(Alignment.BottomCenter)
                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(ParagraphStyle()) {
                                    append("首次运行需要初始化配置,请稍候")
                                }
                                withStyle(ParagraphStyle()) {
                                    append("下载中")
                                    append(
                                        downloading.toBigDecimal()
                                            .setScale(2, RoundingMode.HALF_DOWN).toString()
                                    )
                                    append("%")
                                }
                            },
                            modifier = Modifier.padding(12.dp).fillMaxWidth()
                                .align(Alignment.Center),
                            textAlign = TextAlign.Center
                        )
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