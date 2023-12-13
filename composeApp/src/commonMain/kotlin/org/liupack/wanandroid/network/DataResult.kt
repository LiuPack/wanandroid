package org.liupack.wanandroid.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.liupack.wanandroid.network.exception.DataResultException

@Serializable
data class DataResult<out T>(
    @SerialName("errorCode")
    val code: Int,
    @SerialName("errorMsg")
    val msg: String,
    @SerialName("data")
    val `data`: T
) {
    companion object {
        inline val <T> DataResult<T>.isSuccess get() = code == NetworkConfig.Success
        inline val <T> DataResult<T>.isLoginExpired get() = code == NetworkConfig.NoLogin
        inline val <T> DataResult<T>.catchData
            get() = if (isSuccess) {
                data
            } else {
                throw DataResultException(code, msg)
            }
    }
}
