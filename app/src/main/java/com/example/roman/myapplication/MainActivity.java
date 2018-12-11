package com.example.roman.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Source.DBHelper;
import Source.TaskCard;
import Source.TaskCardAdapter;

public class MainActivity extends AppCompatActivity {
    private int year, month, day;
    private String fullDate;
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
                fullDate = dayOfMonth + "-" + month + "-" + year;

            }
        });
    }

    public void OnClick(View v){
        LinearLayout sv = (LinearLayout)findViewById(R.id.Scroll_layout);
        RecyclerView rv = (RecyclerView)findViewById(R.id.recyclerView);
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String [] args = new String[]{ fullDate };
        List<TaskCard> cards = new ArrayList<>();
        Cursor cursor = db.query("tasks", null, "task_deadline = ?", args
        ,null, null, "task_id");
        if(cursor.moveToFirst()){
            String str;
            do {
//                str = "";
//                for(String columnName : cursor.getColumnNames()){
//                    str = str.concat(columnName + ": " + cursor.getString(cursor.getColumnIndex(columnName)));
//                }
                cards.add(new TaskCard(
                    cursor.getString(cursor.getColumnIndex("task_deadline")),
                    cursor.getString(cursor.getColumnIndex("task_run_time")).toString(),
                    cursor.getString(cursor.getColumnIndex("task_comment"))
                ));
            } while (cursor.moveToNext());
        }
        TaskCardAdapter taskCardAdapter = new TaskCardAdapter(cards);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(taskCardAdapter);
    }
    /*
        метод нажатия на кнопку добавления задачи
     */
    public void OpenNewTask(View v){
        Intent intent = new Intent(MainActivity.this, NewTask.class);
        intent.putExtra("day", this.day);
        intent.putExtra("month", this.month);
        intent.putExtra("year", this.year);
        startActivity(intent);
    }
}
