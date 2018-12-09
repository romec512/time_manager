package com.example.roman.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private int year, month, day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CalendarView cw = (CalendarView) findViewById(R.id.calendarView);
        cw.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                MainActivity.this.day = dayOfMonth;
                MainActivity.this.month = month;
                MainActivity.this.year = year;
            }
        });
    }

    public void OnClick(View v){
        TextView tv = new TextView(MainActivity.this);
        ScrollView sv = (ScrollView)findViewById(R.id.ScrollView);
        sv.addView(tv);
        tv.setText("Сегодняшняя дата :" + day + " - "  + MainActivity.this.month + " - " + MainActivity.this.year);
    }
}
