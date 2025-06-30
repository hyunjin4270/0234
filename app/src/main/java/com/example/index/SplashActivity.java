package com.example.index;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private boolean hasTapped = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView logo = findViewById(R.id.logoText);
        LinearLayout buttonGroup = findViewById(R.id.buttonGroup);
        RelativeLayout splashRoot = findViewById(R.id.splash_root);

        // 로고 페이드인 애니메이션
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logo.startAnimation(fadeIn);

        splashRoot.setOnClickListener(v -> {
            if (!hasTapped) {
                hasTapped = true;
                buttonGroup.setVisibility(View.VISIBLE);
                Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
                buttonGroup.startAnimation(slideUp);
            }
        });

        findViewById(R.id.adminLoginBtn).setOnClickListener(v -> {
            startActivity(new Intent(SplashActivity.this, AdminLoginActivity.class));
        });

        findViewById(R.id.userEnterBtn).setOnClickListener(v -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        });
    }
}
