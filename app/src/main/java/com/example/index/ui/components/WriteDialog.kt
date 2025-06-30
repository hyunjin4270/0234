package com.example.index.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WriteDialog(
    onDismiss: () -> Unit,
    onUploadConfirmed: () -> Unit
) {
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var content by remember { mutableStateOf(TextFieldValue("")) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFAAAAAA))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            // 오른쪽 상단 닫기 버튼
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "닫기")
                }
            }

            // 제목 입력
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text("제목을 입력해주세요.") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(10.dp),
                singleLine = true
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp,
                color = Color.DarkGray
            )

            // 내용 입력
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                placeholder = { Text("내용을 입력해주세요.") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(10.dp),
                maxLines = 10
            )

            Text("최대 100byte", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.align(Alignment.End))

            Spacer(modifier = Modifier.height(12.dp))

            // 파일 첨부 섹션
            Text("파일 첨부:", fontSize = 14.sp)
            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                IconButton(onClick = { /* 이미지 업로드 */ }) {
                    Icon(Icons.Default.Image, contentDescription = "이미지 첨부")
                }
                IconButton(onClick = { /* 비디오 업로드 */ }) {
                    Icon(Icons.Default.Videocam, contentDescription = "비디오 첨부")
                }
            }

            // 미리보기 영역 (임시로 2칸)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Image, contentDescription = "첨부 이미지")
                }

                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Image, contentDescription = "첨부 이미지")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 업로드 버튼
            Button(
                onClick = onUploadConfirmed,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("업로드")
            }
        }
    }
}
