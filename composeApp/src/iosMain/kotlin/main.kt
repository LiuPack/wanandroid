import androidx.compose.ui.window.ComposeUIViewController
import org.liupack.wanandroid.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
