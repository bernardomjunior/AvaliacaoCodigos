package com.android.nbody;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private Repository repo = new Repository();
    private Nbody nbody = new Nbody();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        repo.start(response -> {
            nbody.run();
            repo.finish();
        });
    }
}