package org.liupack.wanandroid.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun LoadingDialog(isVisibility: Boolean) {
    var isShow by remember { mutableStateOf(isVisibility) }
    if (isVisibility) {
        Dialog(onDismissRequest = {
            isShow = false
        }, content = {
            Box(
                modifier = Modifier.size(200.dp)
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.large),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(50.dp))
            }
        })
    }
}

@Composable
fun MessageDialog(isVisibility: Boolean, message: String, hide: () -> Unit = {}) {
    var isShow by remember { mutableStateOf(isVisibility) }
    if (isShow) {
        Dialog(
            onDismissRequest = {
                isShow = false
                hide.invoke()
            }, content = {
                Column(
                    modifier = Modifier.fillMaxWidth().clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.large)
                        .padding(12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(top = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.WarningAmber,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = "温馨提示",
                            style = MaterialTheme.typography.titleMedium,
                            color = contentColorFor(MaterialTheme.colorScheme.surface)
                        )
                    }
                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
                        Text(message, color = contentColorFor(MaterialTheme.colorScheme.surface))
                    }
                    TextButton(onClick = {
                        isShow = false
                        hide.invoke()
                    }, content = {
                        Text("确认")
                    }, modifier = Modifier.align(Alignment.End))
                }
            })
    }
}