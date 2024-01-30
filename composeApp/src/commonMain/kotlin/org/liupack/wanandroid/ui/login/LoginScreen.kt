package org.liupack.wanandroid.ui.login

import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.liupack.wanandroid.common.Logger
import org.liupack.wanandroid.composables.IconBackButton
import org.liupack.wanandroid.composables.LoadingDialog
import org.liupack.wanandroid.composables.MessageDialog
import org.liupack.wanandroid.model.UiState
import org.liupack.wanandroid.model.entity.UserInfoData
import org.liupack.wanandroid.router.Router

fun RouteBuilder.loginScreen(navigator: Navigator) {
    scene(
        route = Router.Login.path, navTransition = NavTransition(
            createTransition = expandHorizontally(),
            destroyTransition = shrinkHorizontally(),
        )
    ) {
        LoginScreen(navigator = navigator)
    }
}

@Composable
fun LoginScreen(navigator: Navigator) {
    val viewModel = koinViewModel(LoginViewModel::class)
    val userName by viewModel.userNameInput.collectAsState()
    val password by viewModel.passwordInput.collectAsState()
    val loginState by viewModel.loginState.collectAsState(null)
    LoginContent(back = { navigator.goBack() },
        userName = userName,
        password = password,
        loginState = loginState,
        onUserNamChange = {
            viewModel.dispatch(LoginAction.UserNameInput(it))
        },
        onPasswordChange = {
            viewModel.dispatch(LoginAction.PasswordInput(it))
        },
        register = { navigator.navigate(Router.Register.path) },
        login = {
            viewModel.dispatch(LoginAction.Login(userName = userName, password = password))
        },
        loginSuccess = { navigator.goBackWith(true) })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginContent(
    back: () -> Unit = {},
    userName: String,
    password: String,
    loginState: UiState<UserInfoData?>?,
    onUserNamChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    register: () -> Unit = {},
    login: () -> Unit = {},
    loginSuccess: () -> Unit = {}
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        MediumTopAppBar(title = { Text("登录") }, navigationIcon = {
            IconBackButton(onClick = back)
        }, scrollBehavior = scrollBehavior)
    }) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 56.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                UserNameInput(userName = userName, onUserNamChange = onUserNamChange)
                PasswordInput(password = password, onPasswordChange = onPasswordChange)
                LoginWithState(
                    loginState = loginState, login = login, loginSuccess = loginSuccess
                )
                TextButton(onClick = register, content = { Text("注册账号") })
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Wan Android",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineLarge.copy(
                    brush = Brush.linearGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary,
                            MaterialTheme.colorScheme.tertiary
                        )
                    ), drawStyle = Stroke(3f)
                ),
                modifier = Modifier.padding(vertical = 24.dp)
            )
        }
    }
}

@Composable
private fun UserNameInput(userName: String, onUserNamChange: (String) -> Unit) {
    OutlinedTextField(
        value = userName,
        onValueChange = onUserNamChange,
        shape = MaterialTheme.shapes.medium,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text,
        ),
        singleLine = true,
        label = {
            Text("登录账号")
        },
        placeholder = {
            Text("请输入登录账号")
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun PasswordInput(password: String, onPasswordChange: (String) -> Unit) {
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        shape = MaterialTheme.shapes.medium,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password,
        ),
        visualTransformation = PasswordVisualTransformation(),
        singleLine = true,
        label = {
            Text("登录密码")
        },
        placeholder = {
            Text("请输入登录密码")
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun LoginWithState(
    loginState: UiState<UserInfoData?>? = null, login: () -> Unit, loginSuccess: () -> Unit
) {
    loginState?.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                LoadingDialog(true)
            }

            is UiState.Exception -> {
                MessageDialog(true, uiState.throwable.message.orEmpty())
            }

            is UiState.Failed -> {
                MessageDialog(isVisibility = true, uiState.message)
            }

            is UiState.Success -> {
                Logger.i("登录成功")
                loginSuccess.invoke()
            }
        }
    }
    Button(modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 12.dp),
        onClick = login,
        content = {
            Text("登录")
        })
}
