package com.example.me1438.yoyo3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    TimerTask timerTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timerTask = new TimerTask() {
            @Override
            public void run() {
                move_login();
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 3000);
    }
    public void move_login() {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }
}
