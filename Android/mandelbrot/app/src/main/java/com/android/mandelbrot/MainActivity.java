package com.android.mandelbrot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private final Repository repository = new Repository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        repository.start(response -> {
            try {
                mandelbrot.run();
                repository.finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}