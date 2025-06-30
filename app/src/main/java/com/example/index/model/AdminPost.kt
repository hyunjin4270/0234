package com.example.index.model

data class AdminPost(
    val title: String = "",
    val content: String = "",
    val imageUrl: String = "",
    val status: String = "대기",
    val timestamp: String = "날짜없음"
)
