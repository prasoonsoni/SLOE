package com.prasoonsoni.supervisedlearninginonlineeducation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class SplashActivity extends AppCompatActivity {

    TextView introText;
    LottieAnimationView introAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        introText = findViewById(R.id.introText);
        introAnimation = findViewById(R.id.introAnimation);

        introText.animate().alpha(1f).setDuration(1500);
        introText.animate().translationYBy(2000f).setDuration(600);
        introAnimation.animate().alpha(1f).setDuration(1500);
        introAnimation.animate().translationYBy(-2000f).setDuration(600);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        },3100);
    }
}