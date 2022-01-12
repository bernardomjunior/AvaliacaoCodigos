package com.android.regex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    RegexRedux regex = new RegexRedux();
    Repository repo = new Repository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        repo.start(response -> {
            try {
                regex.run(getAssets().open("1500000.txt"));
                repo.finish();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}