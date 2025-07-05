package com.example.index.ui.write

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.index.ui.theme.IndexTheme
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class WriteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IndexTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    WriteScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteScreen() {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var uploading by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("글쓰기", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("제목") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("내용") },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (title.isBlank() || content.isBlank()) {
                    Toast.makeText(context, "제목과 내용을 모두 입력해주세요", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                uploading = true

                val newPost = hashMapOf(
                    "title" to title,
                    "content" to content,
                    "imageUrl" to "",  // 나중에 이미지 업로드 기능 연동 가능
                    "videoUrl" to "",  // 나중에 영상 업로드 기능 연동 가능
                    "status" to "대기",
                    "timestamp" to Date()
                )

                FirebaseFirestore.getInstance().collection("user_posts")
                    .add(newPost)
                    .addOnSuccessListener {
                        Toast.makeText(context, "업로드 성공", Toast.LENGTH_SHORT).show()
                        title = ""
                        content = ""
                        uploading = false
                    }
                    .addOnFailureListener { e ->
                        Log.e("WriteActivity", "업로드 실패: ${e.message}")
                        Toast.makeText(context, "업로드 실패", Toast.LENGTH_SHORT).show()
                        uploading = false
                    }
            },
            enabled = !uploading,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(if (uploading) "업로드 중..." else "작성 완료")
        }
    }
}
