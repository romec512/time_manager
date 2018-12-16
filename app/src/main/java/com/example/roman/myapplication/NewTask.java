package com.example.roman.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import Source.DBHelper;
import Source.TimeManager;
import Source.TimeManagerRec;

public class NewTask extends AppCompatActivity implements View.OnClickListener{

    Button btnSave;
    EditText taskRunTime, taskComment;
    SeekBar ratingBar;
    TextView selectedDay;
    DBHelper dbHelper;

    String deadline, day, month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        taskComment = (EditText) findViewById(R.id.etQuestion);
        taskRunTime = (EditText) findViewById(R.id.etTime);
        ratingBar = (SeekBar) findViewById(R.id.rating2);
        ratingBar.setMax(4);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        dbHelper = new DBHelper(this);
        deadline = getIntent().getStringExtra("full_date");
        selectedDay = (TextView)findViewById(R.id.selected_day);
        day = getIntent().getStringExtra("day");
        month = getIntent().getStringExtra("month");
        selectedDay.setText(day + " " + month);
    }

    @Override
    public void onClick(View view) {
        String question = "";
        int time = 0;
        int rating = 1;
        try {
            question = taskComment.getText().toString();
            time = Integer.parseInt(taskRunTime.getText().toString());
            rating = ratingBar.getProgress();
        } catch (Exception e){
            Toast.makeText(this, "Вы неправильно ввели данные, проверьте их и попробуйте снова", Toast.LENGTH_SHORT).show();
            return;
        }
        TimeManagerRec tm = new TimeManagerRec(dbHelper, deadline);
        Date today = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        tm.timing(time, format1.format(today));
        if(tm.isPossible) {
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("task_comment", question);
            contentValues.put("task_run_time", time);
            contentValues.put("task_deadline", deadline);
            contentValues.put("task_priority", rating + 1);
            long rowId = database.insert("tasks", null, contentValues);
            for(String[] task : tm.timingResults){
                ContentValues cv = new ContentValues();
                cv.put("task_id", rowId);
                cv.put("start_time", task[0]);
                cv.put("stop_time", task[1]);
                cv.put("task_date", task[2]);
                database.insert("tasks_distribution", null, cv);
            }
        } else {
            Toast.makeText(this, "К сожалению в вашем расписании не хватает " + tm.impossibleTime +
                    " часов для добавления этой задачи", Toast.LENGTH_SHORT).show();
            return ;
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("new_task_created", true);
        intent.putExtra("deadline", deadline);
        intent.putExtra("comment", question);
        intent.putExtra("month", month);
        startActivity(intent);
    }
}