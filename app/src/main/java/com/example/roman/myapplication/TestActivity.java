package com.example.roman.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {
    private TextView day, month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        day = findViewById(R.id.day);
        month = findViewById(R.id.month);
        year = findViewById(R.id.year);
        day.setText(getIntent().getIntExtra("day", 1) + "");
        month.setText(getIntent().getIntExtra("month", 1) + "");
        year.setText(getIntent().getIntExtra("year", 1970) + "");
        year.setText(getIntent().getIntExtra("year", 1970) + " год");
        year.setText(getIntent().getIntExtra("year", 1970) + " год12");
    }
}
