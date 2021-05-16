package com.prasoonsoni.supervisedlearninginonlineeducation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import pl.droidsonroids.gif.GifImageView;

public class SplashActivity extends AppCompatActivity {

    GifImageView introImage;
    LottieAnimationView introAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        introImage = findViewById(R.id.introImage);
        introAnimation = findViewById(R.id.introAnimation);

        introImage.animate().alpha(1f).setDuration(1500);
        introAnimation.animate().alpha(1f).setDuration(1500);
        introAnimation.animate().translationYBy(-2000f).setDuration(600);


        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        },2600);
    }
}