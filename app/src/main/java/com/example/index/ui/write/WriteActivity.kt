package com.example.index.ui.write

import androidx.activity.compose.rememberLauncherForActivityResult
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.index.ui.theme.IndexTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import java.util.*

class WriteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            IndexTheme {
                WriteScreen(onFinish = { finish() })
            }
        }
    }
}

@Composable
fun WriteScreen(onFinish: () -> Unit) {
    LocalContext.current
    val storage = Firebase.storage
    val db = Firebase.firestore

    var title by remember { mutableStateOf(TextFieldValue("")) }
    var content by remember { mutableStateOf(TextFieldValue("")) }
    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var videoUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        uris.also { imageUris = it }
    }
    val videoPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        videoUris = uris
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFAAAAAA)) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onFinish) {
                    Icon(Icons.Default.Close, contentDescription = "닫기")
                }
            }

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text("제목을 입력해주세요.") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = Color.DarkGray)
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                placeholder = { Text("내용을 입력해주세요.") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(10.dp),
                maxLines = 10
            )

            Text("최대 100byte", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.align(Alignment.End))

            Spacer(modifier = Modifier.height(24.dp))

            Text("파일 첨부:", fontSize = 14.sp)
            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                IconButton(onClick = {
                    imagePickerLauncher.launch("image/*")
                }) {
                    Icon(Icons.Default.Image, contentDescription = "이미지")
                }
                IconButton(onClick = {
                    videoPickerLauncher.launch("video/*")
                }) {
                    Icon(Icons.Default.Videocam, contentDescription = "영상")
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                imageUris.take(2).forEach { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "첨부 이미지",
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.LightGray, shape = RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        val uploadedImageUrls = mutableListOf<String>()
                        val uploadedVideoUrls = mutableListOf<String>()
                        val storageRef = storage.reference

                        imageUris.forEach { uri ->
                            val fileName = UUID.randomUUID().toString()
                            val imageRef = storageRef.child("uploads/images/$fileName.jpg")
                            imageRef.putFile(uri).awaitUpload()
                            val downloadUrl = imageRef.downloadUrl.awaitUrl()
                            uploadedImageUrls.add(downloadUrl.toString())
                        }

                        videoUris.forEach { uri ->
                            val fileName = UUID.randomUUID().toString()
                            val videoRef = storageRef.child("uploads/videos/$fileName.mp4")
                            videoRef.putFile(uri).awaitUpload()
                            val downloadUrl = videoRef.downloadUrl.awaitUrl()
                            uploadedVideoUrls.add(downloadUrl.toString())
                        }

                        val postData = hashMapOf(
                            "title" to title.text,
                            "content" to content.text,
                            "images" to uploadedImageUrls,
                            "videos" to uploadedVideoUrls,
                            "timestamp" to System.currentTimeMillis()
                        )
                        db.collection("posts").add(postData)
                        onFinish()
                    }
                },
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

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun com.google.android.gms.tasks.Task<Uri>.awaitUrl(): Uri {
    return suspendCancellableCoroutine { cont ->
        addOnSuccessListener { cont.resume(it) {} }
        addOnFailureListener { cont.resumeWithException(it) }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun com.google.android.gms.tasks.Task<*>.awaitUpload() {
    return suspendCancellableCoroutine { cont ->
        addOnSuccessListener { cont.resume(Unit) {} }
        addOnFailureListener { cont.resumeWithException(it) }
    }
}
