package org.liupack.wanandroid.ui.register

import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.liupack.wanandroid.composables.IconBackButton
import org.liupack.wanandroid.composables.LoadingDialog
import org.liupack.wanandroid.composables.MessageDialog
import org.liupack.wanandroid.model.UiState
import org.liupack.wanandroid.model.entity.UserInfoData
import org.liupack.wanandroid.router.Router

fun RouteBuilder.registerScreen(navigator: Navigator) {
    scene(
        route = Router.Register.path, navTransition = NavTransition(
            createTransition = expandHorizontally(),
            destroyTransition = shrinkHorizontally(),
        )
    ) {
        RegisterScreen(navigator)
    }
}

@Composable
fun RegisterScreen(navigator: Navigator) {
    val viewModel = koinViewModel(RegisterViewModel::class)
    val userName by viewModel.inputUserName.collectAsState()
    val password by viewModel.inputPassword.collectAsState()
    val rePassword by viewModel.inputRePassword.collectAsState()
    val registerState by viewModel.registerState.collectAsState(null)
    RegisterContent(back = { navigator.goBack() },
        userName = userName,
        password = password,
        rePassword = rePassword,
        onUserNamChange = { viewModel.dispatch(RegisterAction.InputUserName(it)) },
        onPasswordChange = { viewModel.dispatch(RegisterAction.InputPassword(it)) },
        onRePasswordChange = { viewModel.dispatch(RegisterAction.InputRePassword(it)) },
        registerState = registerState,
        register = {
            viewModel.dispatch(
                RegisterAction.Register(
                    userName = viewModel.inputUserName.value,
                    password = viewModel.inputPassword.value,
                    rePassword = viewModel.inputRePassword.value
                )
            )
        },
        registerSuccess = {
            navigator.goBack(PopUpTo(Router.Main.path), false)
        })
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegisterContent(
    back: () -> Unit = {},
    userName: String,
    password: String,
    rePassword: String,
    onUserNamChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onRePasswordChange: (String) -> Unit = {},
    registerState: UiState<UserInfoData?>? = null,
    register: () -> Unit,
    registerSuccess: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        MediumTopAppBar(
            title = { Text(text = "注册账号") },
            navigationIcon = { IconBackButton(onClick = back) },
            scrollBehavior = scrollBehavior
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                UserNameInput(
                    userName = userName, onUserNameChange = onUserNamChange
                )
                PasswordInput(
                    password = password, onPasswordChange = onPasswordChange
                )
                RePasswordInput(
                    password = rePassword, onPasswordChange = onRePasswordChange
                )
                RegisterWithState(
                    registerState = registerState,
                    register = register,
                    registerSuccess = registerSuccess
                )
            }
        }
    }
}

@Composable
fun UserNameInput(userName: String, onUserNameChange: (String) -> Unit) {
    OutlinedTextField(
        value = userName,
        onValueChange = onUserNameChange,
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
fun PasswordInput(password: String, onPasswordChange: (String) -> Unit) {
    var showPassword by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        shape = MaterialTheme.shapes.medium,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password,
        ),
        singleLine = true,
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { showPassword = !showPassword }, content = {
                if (showPassword) {
                    Icon(Icons.Outlined.VisibilityOff, null)
                } else {
                    Icon(Icons.Outlined.Visibility, null)
                }
            })
        },
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
fun RePasswordInput(password: String, onPasswordChange: (String) -> Unit) {
    var showPassword by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        shape = MaterialTheme.shapes.medium,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password,
        ),
        singleLine = true,
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { showPassword = !showPassword }, content = {
                if (showPassword) {
                    Icon(Icons.Outlined.VisibilityOff, null)
                } else {
                    Icon(Icons.Outlined.Visibility, null)
                }
            })
        },
        label = {
            Text("确认登录密码")
        },
        placeholder = {
            Text("请再次输入登录密码")
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun RegisterWithState(
    registerState: UiState<UserInfoData?>? = null,
    register: () -> Unit,
    registerSuccess: () -> Unit,
) {
    registerState?.let { uiState ->
        when (uiState) {
            is UiState.Exception -> {
                MessageDialog(true, uiState.throwable.message.orEmpty())
            }

            is UiState.Failed -> {
                MessageDialog(true, uiState.message)
            }

            is UiState.Loading -> {
                LoadingDialog(true)
            }

            is UiState.Success -> {
                registerSuccess.invoke()
            }
        }
    }
    Button(modifier = Modifier.fillMaxWidth(), onClick = {
        register.invoke()
    }, content = { Text("注册账号") })
}