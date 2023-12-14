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
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadingDialog(isVisibility: Boolean) {
    var isShow by remember { mutableStateOf(isVisibility) }
    if (isVisibility) {
        AlertDialog(modifier = Modifier.fillMaxWidth(), onDismissRequest = {
            isShow = false
        }, content = {
            Box(
                modifier = Modifier.size(100.dp)
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.large),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(48.dp))
            }
        })
    }
}

@Composable
fun MessageDialog(isVisibility: Boolean, message: String) {
    var isShow by remember { mutableStateOf(isVisibility) }
    if (isShow) {
        Dialog(
            onDismissRequest = {
                isShow = false
            }, content = {
                Column(
                    modifier = Modifier.fillMaxWidth().clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.large)
                        .padding(12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(top = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                        Text(text = "温馨提示", style = MaterialTheme.typography.titleMedium)
                    }
                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
                        Text(message)
                    }
                    TextButton(onClick = {
                        isShow = false
                    }, content = {
                        Text("确认")
                    }, modifier = Modifier.align(Alignment.End))
                }
            })
    }
}