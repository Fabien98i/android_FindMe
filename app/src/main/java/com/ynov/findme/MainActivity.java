package com.ynov.findme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private Timer myTimer;
    private final int DELAY = 3000;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
            myTimer = new Timer();
            myTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent myIntent = new Intent (MainActivity.this, MapsActivity.class);
                    startActivity(myIntent);
                    finish();
                }
            }, 2000); */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent myIntent = new Intent (MainActivity.this, SearchType.class);
                startActivity(myIntent);
                finish();
            }
        }, DELAY);
    }
}