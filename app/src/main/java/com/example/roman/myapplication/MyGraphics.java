package com.example.roman.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import Source.DBHelper;
import Source.TaskCard;
import Source.TaskCardAdapter;

import static java.lang.Integer.parseInt;

public class MyGraphics extends AppCompatActivity implements RadialTimePickerDialogFragment.OnTimeSetListener{
    TextView tvMonFirst;
    int [] start_times = new int [] {R.id.start_time_mon, R.id.start_time_tu, R.id.start_time_tu,
            R.id.start_time_wed, R.id.start_time_th, R.id.start_time_fri, R.id.start_time_sat, R.id.start_time_sun};


    int [] end_times = new int [] {R.id.end_time_mon, R.id.end_time_tu, R.id.end_time_tu,
            R.id.end_time_wed, R.id.end_time_th, R.id.end_time_fri, R.id.end_time_sat, R.id.end_time_sun};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_graphics);

        DBHelper dbHelper = new DBHelper(  this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<TaskCard> cards = new ArrayList<>();
        Cursor cursor = db.query("free_time", null, "free_time_variant = 3", null
                ,null, null, "day_of_week");
        if(cursor != null) {
            if (cursor.moveToFirst()) {
                String str;
                int i = 0;
                do {
                    TextView tv = (TextView) findViewById(start_times[i]);
                    tv.setText(cursor.getString(cursor.getColumnIndex("time_start")));
                    tv = (TextView) findViewById(end_times[i]);
                    tv.setText(cursor.getString(cursor.getColumnIndex("time_stop")));
                    i++;
                } while (cursor.moveToNext());
            }
        }
    }

    public void onClick(View v){
        int id = v.getId();
        tvMonFirst = (TextView)findViewById(id);
        String [] currentTime = tvMonFirst.getText().toString().split(":");
        int hours = Integer.parseInt(currentTime[0]);
        int minutes = Integer.parseInt(currentTime[1]);
        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                .setOnTimeSetListener(MyGraphics.this)
                .setStartTime(hours, minutes)
                .setDoneText("Продолжить")
                .setCancelText("Отмена")
                .setThemeLight();
        rtpd.show(getSupportFragmentManager(), "tag");
    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        tvMonFirst.setText(hourOfDay + ":" + (minute / 10) + "" + (minute % 10));
    }

    public void freeTimeSave(View v){
       for(int id = 0; id < 7; id++ ){
            TextView tv  = (TextView) findViewById(start_times[id]);
            String startTime = tv.getText().toString();
            tv = (TextView) findViewById(end_times[id]);
            String endTime = tv.getText().toString();
            if(startTime.length() != 5){
                startTime = "0" + startTime;
            }
            if(endTime.length() != 5){
                endTime = "0" + endTime;
            }
           DBHelper dbHelper = new DBHelper(this);
           SQLiteDatabase db = dbHelper.getWritableDatabase();
           ContentValues cv = new ContentValues();
           if(endTime.compareTo(startTime) < 0){
               Toast.makeText(this, "Вы хотите закончить раньше, чем начать?", Toast.LENGTH_SHORT).show();
               return;
           }
           cv.put("day_of_week", id);
           cv.put("time_start", startTime);
           cv.put("time_stop", endTime);
           cv.put("free_time_variant", 3);
           //ToDo: 7 запросов это очень плохо, надо оптимизировать
           Cursor cursor = db.query("free_time", null, "day_of_week = " + id + " AND free_time_variant = 3", null
                   ,null, null, "day_of_week");
           if (cursor != null) {
               if (!cursor.moveToFirst()) {
                   db.insert("free_time", null, cv);
               } else {
                   db.update("free_time", cv, "day_of_week = " + id + " AND free_time_variant = 3", null);
               }
           }
       }
        Toast.makeText(this, "Ваше расписание успешно добавлено!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
