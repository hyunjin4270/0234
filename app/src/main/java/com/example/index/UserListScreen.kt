package com.example.index

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.NavigateBefore
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.index.util.getStatusColor
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*
import com.example.index.ui.components.HelpDialog
import com.example.index.ui.components.WriteDialog
import com.example.index.ui.components.ReadOnlyDialog
import com.example.index.ui.write.WriteActivity

data class UserPost(
    val title: String = "",
    val content: String = "",
    val timestamp: Date? = null,
    val status: String = "대기",
    val imageUrl: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen() {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    var postList by remember { mutableStateOf<List<UserPost>>(emptyList()) }
    var currentPage by remember { mutableStateOf(0) }
    val itemsPerPage = 5
    var previousDocs = remember { mutableStateListOf<DocumentSnapshot?>() }

    var searchQuery by remember { mutableStateOf("") }
    var showSearch by remember { mutableStateOf(false) }
    var showWriteDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }
    var selectedPost by remember { mutableStateOf<UserPost?>(null) }

    suspend fun loadPage(page: Int) {
        try {
            var query = db.collection("user_posts")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(itemsPerPage.toLong())

            if (page > 0 && previousDocs.size >= page) {
                val startAfterDoc = previousDocs[page - 1]
                if (startAfterDoc != null) query = query.startAfter(startAfterDoc)
            }

            val snapshot = query.get().await()
            val docs = snapshot.documents
            if (page == previousDocs.size && docs.isNotEmpty()) {
                previousDocs.add(docs.lastOrNull())
            }

            val posts = docs.mapNotNull { doc ->
                val timestamp = doc.getTimestamp("timestamp")?.toDate()
                UserPost(
                    title = doc.getString("title") ?: "",
                    content = doc.getString("content") ?: "",
                    timestamp = timestamp,
                    status = doc.getString("status") ?: "대기",
                    imageUrl = doc.getString("imageUrl") ?: ""
                )
            }
            postList = posts
        } catch (e: Exception) {
            Log.e("UserListScreen", "불러오기 오류: ${e.message}")
        }
    }

    LaunchedEffect(currentPage) {
        loadPage(currentPage)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("유저 리스트") },
                actions = {
                    IconButton(onClick = { showSearch = !showSearch }) {
                        Icon(Icons.Default.Search, contentDescription = "검색")
                    }
                    IconButton(onClick = { showWriteDialog = true }) {
                        Icon(Icons.Default.Create, contentDescription = "글쓰기")
                    }
                    IconButton(onClick = { showHelpDialog = true }) {
                        Icon(Icons.AutoMirrored.Filled.Help, contentDescription = "도움말")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color(0xFFF9F3F3)) {
                NavigationBarItem(
                    selected = false,
                    onClick = { currentPage = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "처음") },
                    label = { Text("") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { if (currentPage > 0) currentPage-- },
                    icon = { Icon(Icons.AutoMirrored.Filled.NavigateBefore, contentDescription = "이전") },
                    label = { Text("") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        val maxPage = (postList.size + itemsPerPage - 1) / itemsPerPage - 1
                        if (currentPage < maxPage) currentPage++
                    },
                    icon = { Icon(Icons.AutoMirrored.Filled.NavigateNext, contentDescription = "다음") },
                    label = { Text("") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        showWriteDialog = true
                    },
                    icon = { Icon(Icons.Default.Create, contentDescription = "작성") },
                    label = { Text("") }
                )
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            if (showSearch) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("검색어를 입력하세요") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    singleLine = true
                )
            }

            val filteredPosts = postList.filter {
                it.title.contains(searchQuery, true) || it.content.contains(searchQuery, true)
            }

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(filteredPosts) { post ->
                    val formattedDate = post.timestamp?.let {
                        SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(it)
                    } ?: "작성일자 없음"

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(getStatusColor(post.status), RoundedCornerShape(12.dp))
                            .clickable { selectedPost = post }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(post.title, style = MaterialTheme.typography.titleMedium, color = Color.White)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(post.content, style = MaterialTheme.typography.bodyMedium, color = Color.White)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("상태: ${post.status}", style = MaterialTheme.typography.bodySmall, color = Color.White)
                            Text(formattedDate, style = MaterialTheme.typography.labelSmall, color = Color.White)
                        }
                    }
                }
            }
        }
    }

    if (showHelpDialog) {
        HelpDialog(onDismiss = { showHelpDialog = false })
    }

    if (showWriteDialog) {
        WriteDialog(
            onDismiss = { showWriteDialog = false },
            onUploadConfirmed = { title, content, imageUrl, videoUrl ->
                val newPost = hashMapOf(
                    "title" to title,
                    "content" to content,
                    "imageUrl" to imageUrl,
                    "videoUrl" to videoUrl,
                    "status" to "대기",
                    "timestamp" to Date()
                )

                FirebaseFirestore.getInstance().collection("user_posts")
                    .add(newPost)
                    .addOnSuccessListener {
                        showWriteDialog = false
                        currentPage = 0
                    }
                    .addOnFailureListener { e ->
                        Log.e("UserListScreen", "업로드 실패: ${e.message}")
                    }
            }
        )
    }

    selectedPost?.let {
        ReadOnlyDialog(
            title = it.title,
            content = it.content,
            status = it.status,
            timestamp = it.timestamp,
            imageUrl = it.imageUrl,
            onDismiss = { selectedPost = null }
        )
    }
}
