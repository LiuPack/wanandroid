import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.koin.core.context.startKoin
import org.liupack.wanandroid.App
import org.liupack.wanandroid.model.di.appModule
import java.awt.Dimension

fun main() = application {
    startKoin {
        modules(appModule)
    }
    Window(
        title = "wanandroid",
        state = rememberWindowState(width = 800.dp, height = 600.dp),
        onCloseRequest = ::exitApplication,
    ) {
        window.minimumSize = Dimension(350, 600)
        App()
    }
}