package org.liupack.wanandroid.ui.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
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
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import org.liupack.wanandroid.composables.LoadingDialog
import org.liupack.wanandroid.composables.MessageDialog
import org.liupack.wanandroid.model.UiState
import org.liupack.wanandroid.router.Router

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navigator: Navigator) {
    val viewModel = koinViewModel(RegisterViewModel::class)
    val scrollState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(text = "注册账号")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navigator.popBackStack()
                    }, content = { Icon(Icons.Outlined.ArrowBack, null) })
                },
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
                UserNameInput(viewModel)
                PasswordInput(viewModel)
                RePasswordInput(viewModel)
                RegisterWithState(navigator, viewModel)
            }
        }
    }
}

@Composable
fun RegisterWithState(navigator: Navigator, viewModel: RegisterViewModel) {
    val registerState by viewModel.registerState.collectAsStateWithLifecycle(null)
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
                navigator.navigate(Router.Home.path, NavOptions(launchSingleTop = true))
            }
        }
    }
    Button(modifier = Modifier.fillMaxWidth(), onClick = {
        viewModel.dispatch(
            RegisterAction.Register(
                userName = viewModel.inputUserName.value,
                password = viewModel.inputPassword.value,
                rePassword = viewModel.inputRePassword.value
            )
        )
    }, content = { Text("注册账号") })
}

@Composable
fun RePasswordInput(viewModel: RegisterViewModel) {
    val password by viewModel.inputRePassword.collectAsStateWithLifecycle()
    var showPassword by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = password,
        onValueChange = { viewModel.dispatch(RegisterAction.InputRePassword(it)) },
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
fun PasswordInput(viewModel: RegisterViewModel) {
    val password by viewModel.inputPassword.collectAsStateWithLifecycle()
    var showPassword by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = password,
        onValueChange = { viewModel.dispatch(RegisterAction.InputPassword(it)) },
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
fun UserNameInput(viewModel: RegisterViewModel) {
    val userName by viewModel.inputUserName.collectAsStateWithLifecycle()
    OutlinedTextField(
        value = userName,
        onValueChange = { viewModel.dispatch(RegisterAction.InputUserName(it)) },
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
