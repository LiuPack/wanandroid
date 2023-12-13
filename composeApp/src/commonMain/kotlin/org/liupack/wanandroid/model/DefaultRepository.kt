package org.liupack.wanandroid.model

import io.ktor.client.request.forms.submitForm
import io.ktor.http.parameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.liupack.wanandroid.network.NetworkConfig
import org.liupack.wanandroid.network.connect
import org.liupack.wanandroid.network.dataResultBody

class DefaultRepository : Repository {
    override fun login(userName: String, password: String): Flow<Any> {
        return flow<Any> {
            val result = connect().submitForm(NetworkConfig.loginApi, parameters {
                append("username", userName)
                append("password", password)
            }).dataResultBody<Any>()
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    override fun register(userName: String, password: String, rePassword: String): Flow<Any> {
        return flow {
            val result = connect().submitForm(NetworkConfig.registerApi, parameters {
                append("username", userName)
                append("password", password)
                append("repassword", rePassword)
            }).dataResultBody<Any>()
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
}