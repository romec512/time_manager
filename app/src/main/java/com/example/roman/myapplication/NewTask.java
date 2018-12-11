package com.example.roman.myapplication;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import Source.DBHelper;

public class NewTask extends AppCompatActivity implements View.OnClickListener{

    Button btnSave;
    EditText taskRunTime, taskComment;
    RatingBar ratingBar;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        taskComment = (EditText) findViewById(R.id.etQuestion);
        taskRunTime = (EditText) findViewById(R.id.etTime);
        ratingBar = (RatingBar) findViewById(R.id.rating);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        dbHelper = new DBHelper(this);
    }

    @Override
    public void onClick(View view) {
        String question = taskComment.getText().toString();
        int time = Integer.parseInt(taskRunTime.getText().toString());
        float rating = ratingBar.getRating();
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        switch (view.getId()){
            case R.id.btnSave:
                contentValues.put("task_comment", question);
                contentValues.put("task_run_time", time);
                contentValues.put("task_deadline", "12-12-2018");
                contentValues.put("task_priority", (int)rating);

                database.insert("tasks", null, contentValues);
                break;
        }
    }
}