package com.android.pidigits;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Repository repository = new Repository();
    private Pidigits pidigits = new Pidigits();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        repository.start(response -> {
            pidigits.run();
            repository.finish();
        });
    }
}