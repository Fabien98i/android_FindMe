package com.ynov.findme.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.ynov.findme.R;
import com.ynov.findme.ui.home.HomeActivity;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    private Timer myTimer;
    private final int DELAY = 3000;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent myIntent = new Intent (MainActivity.this, HomeActivity.class);
                startActivity(myIntent);
                finish();
            }
        }, DELAY);
    }
}