package com.example.index.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.NavigateBefore
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun HelpDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            color = Color.Black
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // 상단 설명 아이콘
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(Icons.AutoMirrored.Filled.Help, contentDescription = "도움말", tint = Color.White)

                    Icon(Icons.Filled.Search, contentDescription = "검색", tint = Color.White)
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("키워드 검색 →", color = Color.White, fontSize = 14.sp, modifier = Modifier.align(Alignment.End))
                Text("제목을 표기합니다.", color = Color.White, fontSize = 14.sp, modifier = Modifier.align(Alignment.End))
                Spacer(modifier = Modifier.height(32.dp))

                // 샘플 카드
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = MaterialTheme.shapes.medium)
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Image, contentDescription = "이미지")
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text("■■■■■■■■■■■■", fontSize = 18.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row {
                                Button(
                                    onClick = {},
                                    enabled = false,
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                                ) {
                                    Text("대기")
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("작성일자: nnnn/nn/nn", fontSize = 12.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text("현재 상태를 나타냅니다.", color = Color.White, fontSize = 14.sp)
                    Text("글 작성일을 보여줍니다.", color = Color.White, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(32.dp))

                // 네비게이션 설명
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFFF4F4), shape = RoundedCornerShape(32.dp))
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Home, contentDescription = "처음")
                    Icon(Icons.AutoMirrored.Filled.NavigateBefore, contentDescription = "이전")
                    Icon(Icons.AutoMirrored.Filled.NavigateNext, contentDescription = "다음")
                    Icon(Icons.Filled.Create, contentDescription = "작성")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text("첫 화면", color = Color.White)
                    Text("이전 페이지", color = Color.White)
                    Text("다음 페이지", color = Color.White)
                    Text("글쓰기", color = Color.White)
                }
            }
        }
    }
}
