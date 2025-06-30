package com.example.index.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun AdminDialog(
    title: String,
    date: String,
    imageUrl: String,
    content: String,
    currentType: String,
    onTypeChange: (String) -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onDismiss: () -> Unit,
    imageUri: String
) {
    val types = listOf("위험", "주의", "양호")
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box {
                    TextButton(
                        onClick = { expanded = true },
                        modifier = Modifier.background(Color(0xFFF3F3F3))
                    ) {
                        Text(
                            currentType,
                            color = when (currentType) {
                                "위험" -> Color.Red
                                "주의" -> Color(0xFFF9A825)
                                else -> Color.Black
                            }
                        )
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        types.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type) },
                                onClick = {
                                    expanded = false
                                    onTypeChange(type)
                                }
                            )
                        }
                    }
                }
                Spacer(Modifier.weight(1f))
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "삭제")
                }
                IconButton(onClick = onEdit) {
                    Icon(Icons.Filled.Create, contentDescription = "수정")
                }
            }
        },
        text = {
            Column(Modifier.fillMaxWidth()) {
                Text("작성일자: $date", style = MaterialTheme.typography.labelSmall)
                Spacer(Modifier.height(12.dp))
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "이미지",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(Color.LightGray)
                )
                Spacer(Modifier.height(12.dp))
                Text(content, style = MaterialTheme.typography.bodyLarge)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("닫기") }
        }
    )
}
