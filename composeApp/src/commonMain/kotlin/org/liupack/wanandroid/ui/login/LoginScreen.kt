package org.liupack.wanandroid.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.liupack.wanandroid.common.Logger
import org.liupack.wanandroid.composables.LoadingDialog
import org.liupack.wanandroid.composables.MessageDialog
import org.liupack.wanandroid.model.UiState
import org.liupack.wanandroid.model.entity.UserInfoData
import org.liupack.wanandroid.ui.register.RegisterScreen

object LoginScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<LoginViewModel>()
        val userName by viewModel.userNameInput.collectAsState()
        val password by viewModel.passwordInput.collectAsState()
        Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 56.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
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
                    )
                    UserNameInput(userName = userName, onUserNamChange = {
                        viewModel.dispatch(LoginAction.UserNameInput(it))
                    })
                    PasswordInput(password = password, onPasswordChange = {
                        viewModel.dispatch(LoginAction.PasswordInput(it))
                    })
                    LoginWithState(navigator) {
                        viewModel.dispatch(
                            LoginAction.Login(
                                userName = viewModel.userNameInput.value,
                                password = viewModel.passwordInput.value
                            )
                        )
                    }
                    TextButton(onClick = {
                        navigator.push(RegisterScreen)
                    }, content = { Text("注册账号") })
                }
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
        navigator: Navigator = LocalNavigator.currentOrThrow,
        loginState: UiState<UserInfoData?>? = null,
        login: () -> Unit,
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
                    navigator.pop()
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
}
