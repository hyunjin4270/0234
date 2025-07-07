package com.example.index.ui.components

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.storage.FirebaseStorage
import java.util.*

@Composable
fun WriteDialog(
    onDismiss: () -> Unit,
    onUploadConfirmed: (String, String, String, String) -> Unit
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf(TextFieldValue()) }
    var content by remember { mutableStateOf(TextFieldValue()) }
    var imageUrl by remember { mutableStateOf("") }
    var videoUrl by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<android.net.Uri?>(null) }
    var videoUri by remember { mutableStateOf<android.net.Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: android.net.Uri? ->
        uri?.let { imageUri = it }
    }

    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: android.net.Uri? ->
        uri?.let { videoUri = it }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFF1F1F1),
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "닫기")
                    }
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("제목을 입력해주세요.") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    placeholder = { Text("내용을 입력해주세요.") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .border(1.dp, Color(0xFFB67AFF), RoundedCornerShape(8.dp))
                        .padding(4.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("파일 첨부", modifier = Modifier.align(Alignment.CenterVertically))
                    IconButton(onClick = { imagePickerLauncher.launch("image/*") }) {
                        Icon(Icons.Default.Image, contentDescription = "이미지")
                    }
                    IconButton(onClick = { videoPickerLauncher.launch("video/*") }) {
                        Icon(Icons.Default.Videocam, contentDescription = "비디오")
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.LightGray, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        imageUri?.let {
                            Image(
                                painter = rememberAsyncImagePainter(it),
                                contentDescription = "이미지 미리보기",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } ?: Icon(Icons.Default.Image, contentDescription = null)
                    }

                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.LightGray, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        videoUri?.let {
                            Text("비디오 선택됨")
                        } ?: Icon(Icons.Default.Videocam, contentDescription = null)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    val storage = FirebaseStorage.getInstance("gs://project-4795969965920072181.firebasestorage.app")
                    val uploads = mutableListOf<Pair<String, android.net.Uri>>()
                    imageUri?.let { uploads.add("image" to it) }
                    videoUri?.let { uploads.add("video" to it) }

                    var uploaded = 0
                    val urls = mutableMapOf<String, String>()

                    if (uploads.isEmpty()) {
                        onUploadConfirmed(title.text, content.text, "", "")
                        return@Button
                    }

                    uploads.forEach { (type, uri) ->
                        val fileRef = storage.reference.child("uploads/${UUID.randomUUID()}")
                        fileRef.putFile(uri)
                            .continueWithTask { task ->
                                if (!task.isSuccessful) {
                                    throw task.exception ?: Exception("업로드 실패")
                                }
                                fileRef.downloadUrl
                            }
                            .addOnSuccessListener { url ->
                                urls[type] = url.toString()
                                uploaded++
                                if (uploaded == uploads.size) {
                                    onUploadConfirmed(
                                        title.text,
                                        content.text,
                                        urls["image"] ?: "",
                                        urls["video"] ?: ""
                                    )
                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "파일 업로드 실패: ${it.message}", Toast.LENGTH_SHORT).show()
                                Log.e("WriteDialog", "Storage upload failed", it)
                            }
                    }
                }) {
                    Text("업로드")
                }
            }
        }
    }
}
