package com.android.binarytree;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private final Repository repo = new Repository();
    private final Binarytrees binarytrees = new Binarytrees();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        repo.start(response -> {
            try {
                binarytrees.run(19);
                repo.finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}