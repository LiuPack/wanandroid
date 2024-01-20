package org.liupack.wanandroid.model.entity

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class NullData(
    val message: String? = "",
)