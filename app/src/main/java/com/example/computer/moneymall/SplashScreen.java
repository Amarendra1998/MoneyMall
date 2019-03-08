package com.example.computer.moneymall;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class SplashScreen extends AppCompatActivity {
    TextView t;
    CircleImageView circleImageView;
    Animation uptodown,downtoup;
    private static int SPLASH_TIME_OUT = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        circleImageView = (CircleImageView) findViewById(R.id.circle);
        t= (TextView)findViewById(R.id.textview);
        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        circleImageView.setAnimation(uptodown);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        t.setAnimation(downtoup);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(SplashScreen.this,LoginActivity.class);
                startActivity(homeIntent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
