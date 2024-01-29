package org.liupack.wanandroid.composables

import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun IconBackButton(onClick: () -> Unit, imageVector: ImageVector? = null) {
    IconButton(onClick = onClick, content = {
        Icon(
            imageVector = imageVector ?: Icons.Outlined.ArrowBack,
            contentDescription = null
        )
    })
}