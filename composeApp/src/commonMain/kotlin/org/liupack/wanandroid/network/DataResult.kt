package org.liupack.wanandroid.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.liupack.wanandroid.network.exception.DataResultException
import org.liupack.wanandroid.network.exception.LoginExpiredException

@Serializable
data class DataResult<out T : Any>(
    @SerialName("errorCode")
    val code: Int,
    @SerialName("errorMsg")
    val msg: String,
    @SerialName("data")
    val `data`: T? = null
) {
    companion object {
        inline val <T : Any> DataResult<T>.isSuccess get() = code == NetworkConfig.Success
        inline val <T : Any> DataResult<T>.isLoginExpired get() = code == NetworkConfig.NoLogin
        inline val <T : Any> DataResult<T>.catchData
            get() = if (isSuccess) {
                data
            } else {
                if (isLoginExpired) {
                    throw LoginExpiredException(code, msg)
                }
                throw DataResultException(code, msg)
            }
    }
}
