package com.rzm.matrix_testing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rzm.matrix_testing.hooks.TestHooksActivity;
import com.rzm.matrix_testing.io.TestIOActivity;
import com.rzm.matrix_testing.resources.TestLeakActivity;
import com.rzm.matrix_testing.trace.TestTraceMainActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button testTrace = findViewById(R.id.test_trace);
        testTrace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestTraceMainActivity.class);
                startActivity(intent);
            }
        });
        Button testIo = findViewById(R.id.test_io);
        testIo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestIOActivity.class);
                startActivity(intent);
            }
        });
        Button testLeak = findViewById(R.id.test_leak);
        testLeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestLeakActivity.class);
                startActivity(intent);
            }
        });
        Button testHooks = findViewById(R.id.test_hooks);
        testHooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestHooksActivity.class);
                startActivity(intent);
            }
        });
    }
}