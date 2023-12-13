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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import org.liupack.wanandroid.Constants
import org.liupack.wanandroid.platform.settings

@Composable
fun LoginScreen(navigator: Navigator) {
    val viewModel = koinViewModel(LoginViewModel::class)
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
                UserNameInput(viewModel)
                PasswordInput(viewModel)
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    onClick = {
                        settings.putBoolean(Constants.isLogin, true)
                        navigator.goBackWith(true)
                    },
                    content = {
                        Text("登录")
                    })
            }
        }
    }
}


@Composable
fun UserNameInput(viewModel: LoginViewModel) {
    val userName by viewModel.userNameInput.collectAsStateWithLifecycle()
    OutlinedTextField(
        value = userName,
        onValueChange = { viewModel.dispatch(LoginAction.UserNameInput(it)) },
        shape = MaterialTheme.shapes.medium,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text,
        ), label = {
            Text("登录账号")
        }, placeholder = {
            Text("请输入登录账号")
        }, modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun PasswordInput(viewModel: LoginViewModel) {
    val password by viewModel.passwordInput.collectAsStateWithLifecycle()
    OutlinedTextField(
        value = password,
        onValueChange = { viewModel.dispatch(LoginAction.PasswordInput(it)) },
        shape = MaterialTheme.shapes.medium,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password,
        ),
        visualTransformation = PasswordVisualTransformation(),
        label = {
            Text("登录密码")
        }, placeholder = {
            Text("请输入登录密码")
        }, modifier = Modifier.fillMaxWidth()
    )
}