package com.android.fannkuch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private Repository repository = new Repository();
    private fannkuchredux fannkuch = new fannkuchredux();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        repository.start(response -> {
            fannkuch.runCode();
            repository.finish();
        });
    }
}