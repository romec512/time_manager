package com.example.roman.myapplication;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import Source.DBHelper;

public class NewTask extends AppCompatActivity implements View.OnClickListener{

    Button btnSave;
    EditText etTime, etQuestion;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etQuestion = (EditText) findViewById(R.id.etQuestion);
        etTime = (EditText) findViewById(R.id.etTime);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        dbHelper = new DBHelper(this);
    }

    @Override
    public void onClick(View view) {
        String question = etQuestion.getText().toString();
        String time = etTime.getText().toString();
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        switch (view.getId()){
            case R.id.btnSave:
                contentValues.put(DBHelper.KEY_QUESTION, question);
                contentValues.put(DBHelper.KEY_TIME, time);

                database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);
                break;
        }
    }
}