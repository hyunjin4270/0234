package com.example.index

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.index.ui.theme.IndexTheme

class AdminListActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IndexTheme {
                AdminListScreen() // ✅ 괄호 안에 아무것도 전달하지 않음
            }
        }

    }
}
