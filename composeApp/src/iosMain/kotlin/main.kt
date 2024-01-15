import androidx.compose.ui.window.ComposeUIViewController
import org.koin.core.context.startKoin
import org.liupack.wanandroid.App
import org.liupack.wanandroid.model.di.appModule
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController {
    startKoin {
        modules(appModule)
        allowOverride(false)
    }
    App()
}