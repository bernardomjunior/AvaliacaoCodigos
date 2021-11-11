package com.android.spectral;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private Repository repo = new Repository();
    private Spectral spectral = new Spectral();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        repo.start(response -> {
            spectral.run();
            repo.finish();
        });
    }
}