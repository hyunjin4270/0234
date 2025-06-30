package com.example.index

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.index.ui.theme.IndexTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IndexTheme {
                VideoBackgroundScreen()
            }
        }
    }
}

@Composable
fun VideoBackgroundScreen() {
    val context = LocalContext.current

    // 🎞️ ExoPlayer 세팅
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            val uri = "asset:///introform.mp4".toUri()
            setMediaItem(MediaItem.fromUri(uri))
            repeatMode = ExoPlayer.REPEAT_MODE_ALL
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 🎬 배경 영상
        AndroidView(
            factory = {
                PlayerView(it).apply {
                    useController = false
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    this.player = player
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // 🎨 UI 요소
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("환영합니다!", color = Color.White, style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(20.dp))

            // ✅ 일반 회원 버튼 → UserListActivity로 이동
            Button(onClick = {
                val intent = Intent(context, UserListActivity::class.java)
                context.startActivity(intent)
            }) {
                Text("일반 회원으로 시작")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ 관리자 로그인 버튼 → AdminLoginActivity로 이동
            OutlinedButton(onClick = {
                val intent = Intent(context, AdminLoginActivity::class.java)
                context.startActivity(intent)
            }) {
                Text("관리자 로그인")
            }
        }
    }
}
