package org.liupack.wanandroid.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class NullData(
    val message: String? = "",
)