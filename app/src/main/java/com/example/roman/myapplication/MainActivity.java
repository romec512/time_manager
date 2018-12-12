package com.example.roman.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import Source.DBHelper;

public class MainActivity extends AppCompatActivity {
    private int year, month, day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageView icon = new ImageView(this);
        icon.setImageResource(R.drawable.add);
        final FloatingActionButton fab = new FloatingActionButton.Builder(this).setContentView(icon).build();

        SubActionButton.Builder builder = new SubActionButton.Builder(this);

        ImageView addIcon = new ImageView(this);
        addIcon.setImageResource(R.drawable.add_green);
        SubActionButton add = builder.setContentView(addIcon).build();

        ImageView playIcon = new ImageView(this);
        playIcon.setImageResource(R.drawable.play);
        SubActionButton play = builder.setContentView(playIcon).build();

        ImageView removeIcon = new ImageView(this);
        removeIcon.setImageResource(R.drawable.remove);
        SubActionButton remove = builder.setContentView(removeIcon).build();



        final FloatingActionMenu fam = new FloatingActionMenu.Builder(this)
                .addSubActionView(add)
                .addSubActionView(play)
                .addSubActionView(remove)
                .attachTo(fab)
                .build();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Вы нажали Add", Toast.LENGTH_SHORT).show();
                fam.close(true);
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Вы нажали Play", Toast.LENGTH_SHORT).show();
                fam.close(true);
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Вы нажали remove", Toast.LENGTH_SHORT).show();
                fam.close(true);
            }
        });



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
        LinearLayout sv = (LinearLayout)findViewById(R.id.Scroll_layout);
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("tasks", null, null,null,null, null, "task_id");
        if(cursor.moveToFirst()){
            String str;
            do {
                str = "";
                for(String columnName : cursor.getColumnNames()){
                    str = str.concat(columnName + ": " + cursor.getString(cursor.getColumnIndex(columnName)));
                }
                TextView tv = new TextView(MainActivity.this);
                tv.setText(str);
                sv.addView(tv);
            } while (cursor.moveToNext());
        }
    }
    /*
        метод нажатия на кнопку добавления задачи
     */
    public void OpenNewTask(View v){
        Intent intent = new Intent(MainActivity.this, NewTask.class);
        startActivity(intent);
    }
}
