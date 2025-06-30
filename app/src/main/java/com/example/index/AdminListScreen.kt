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
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.index.ui.components.HelpDialog
import com.example.index.ui.write.WriteActivity
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminListScreen() {
    val context = LocalContext.current
    val db = Firebase.firestore

    var allItems by remember { mutableStateOf(listOf<String>()) }
    var currentPage by remember { mutableIntStateOf(0) }
    var showDetailDialog by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<String?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }
    var showSearchBar by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val itemsPerPage = 5

    DisposableEffect(Unit) {
        val listener: ListenerRegistration = db.collection("posts")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    allItems = snapshot.documents.mapNotNull { it.getString("title") }
                }
            }
        onDispose { listener.remove() }
    }

    val filteredItems = if (searchQuery.isNotBlank()) {
        allItems.filter { it.contains(searchQuery, ignoreCase = true) }
    } else {
        allItems
    }

    val start = currentPage * itemsPerPage
    val end = (start + itemsPerPage).coerceAtMost(filteredItems.size)
    val currentItems = filteredItems.subList(start, end)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (showSearchBar) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("검색어를 입력하세요") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text("")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { showHelpDialog = true }) {
                        Icon(Icons.AutoMirrored.Filled.Help, contentDescription = "도움말")
                    }
                },
                actions = {
                    IconButton(onClick = { showSearchBar = !showSearchBar }) {
                        Icon(Icons.Filled.Search, contentDescription = "검색")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFAAAAAA))
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
                        val maxPage = (filteredItems.size + itemsPerPage - 1) / itemsPerPage - 1
                        if (currentPage < maxPage) currentPage++
                    },
                    icon = { Icon(Icons.AutoMirrored.Filled.NavigateNext, contentDescription = "다음") },
                    label = { Text("") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        val intent = Intent(context, WriteActivity::class.java)
                        context.startActivity(intent)
                    },
                    icon = { Icon(Icons.Default.Create, contentDescription = "작성") },
                    label = { Text("") }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFAAAAAA))
                .padding(innerPadding)
                .padding(horizontal = 12.dp)
        ) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(currentItems) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .clickable {
                                selectedItem = item
                                showDetailDialog = true
                            }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(Color.LightGray, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Image, contentDescription = "이미지")
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = item, style = MaterialTheme.typography.bodyLarge)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Button(
                                    onClick = {},
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD3D3D3)),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text("대기", color = Color.Black, fontSize = 13.sp)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("작성일자: nnnn/nn/nn", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }

        if (showDetailDialog && selectedItem != null) {
            AlertDialog(
                onDismissRequest = { showDetailDialog = false },
                confirmButton = {
                    TextButton(onClick = { showDetailDialog = false }) { Text("닫기") }
                },
                title = { Text("글 상세정보") },
                text = {
                    Column {
                        Text("글 제목: ${selectedItem ?: ""}", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                        Text("작성일자: nnnn/nn/nn", style = MaterialTheme.typography.bodySmall)
                        Spacer(Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .background(Color.LightGray, shape = RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Image, contentDescription = "샘플 이미지")
                        }
                        Spacer(Modifier.height(8.dp))
                        Text("내용: 여기에 글 내용을 출력합니다.", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            )
        }

        if (showDeleteDialog && selectedItem != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        val deleteQuery = db.collection("posts").whereEqualTo("title", selectedItem)
                        deleteQuery.get().addOnSuccessListener { querySnapshot ->
                            for (doc in querySnapshot.documents) {
                                doc.reference.delete()
                            }
                        }
                        showDeleteDialog = false
                    }) { Text("삭제") }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) { Text("취소") }
                },
                title = { Text("삭제 확인") },
                text = { Text("정말 삭제하시겠습니까?") }
            )
        }

        if (showHelpDialog) {
            HelpDialog(onDismiss = { showHelpDialog = false })
        }
    }
}
