package com.example.roman.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView icon = new ImageView(this);
        icon.setImageResource(R.drawable.add);
        final FloatingActionButton fab = new FloatingActionButton.Builder(this).setContentView(icon).build();

        SubActionButton.Builder builder = new SubActionButton.Builder(this);

        ImageView addIcon = new ImageView(this);
        addIcon.setImageResource(R.drawable.addbtn);
        SubActionButton add = builder.setContentView(addIcon).build();

        ImageView playIcon = new ImageView(this);
        playIcon.setImageResource(R.drawable.playbtn);
        SubActionButton play = builder.setContentView(playIcon).build();

        ImageView removeIcon = new ImageView(this);
        removeIcon.setImageResource(R.drawable.pausebtn);
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

        CalendarView cw = (CalendarView) findViewById(R.id.calendarView);
        cw.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("day", dayOfMonth);
                intent.putExtra("test", "test");
                startActivity(intent);
            }
        });
    }
}
