package org.liupack.wanandroid.model

import kotlinx.coroutines.flow.Flow

interface Repository {

    fun login(userName: String, password: String): Flow<Any>

    fun register(userName: String, password: String, rePassword: String): Flow<Any>
}