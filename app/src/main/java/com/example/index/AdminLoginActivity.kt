package com.example.index

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.index.ui.theme.IndexTheme

class AdminLoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IndexTheme {
                AdminLoginScreen(
                    onBackClick = { finish() },
                    onLoginClick = { id, pw ->
                        // TODO: 로그인 처리 로직 작성
                    }
                )
            }
        }
    }
}
