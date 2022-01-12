package com.android.knucleotide;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private final Repository repo = new Repository();
    private final Knucleotide code = new Knucleotide();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        repo.start(response -> {
            try {
                code.run(getAssets().open("knucleotide input - 25000000.txt"));
                repo.finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}