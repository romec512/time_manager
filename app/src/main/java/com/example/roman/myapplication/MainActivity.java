package com.example.roman.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Source.DBHelper;
import Source.TaskCard;
import Source.TaskCardAdapter;

public class MainActivity extends AppCompatActivity {
    private String fullDate, day;
    int month;
    String year;
    private TextView headerSelectedMonthName, selectedDate;
    private String[] months = {"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август",
            "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};
    private String[] monthsWithPostfix = {"Января", "Февраля", "Марта", "Апреля", "Мая", "Июня", "Июля", "Августа",
            "Сентября", "Октября", "Ноября", "Декабря"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        headerSelectedMonthName = (TextView)findViewById(R.id.monthName);
        selectedDate = (TextView)findViewById(R.id.selected_date);

        ImageView icon = new ImageView(this);
        icon.setImageResource(R.mipmap.ic_launcher_round);
        final FloatingActionButton fab = new FloatingActionButton.Builder(this).setContentView(icon).build();

        SubActionButton.Builder builder = new SubActionButton.Builder(this);

        ImageView addIcon = new ImageView(this);
        addIcon.setImageResource(R.drawable.add_button);
        SubActionButton add = builder.setContentView(addIcon).build();

        ImageView playIcon = new ImageView(this);
        playIcon.setImageResource(R.drawable.play_button);
        SubActionButton play = builder.setContentView(playIcon).build();

        ImageView removeIcon = new ImageView(this);
        removeIcon.setImageResource(R.drawable.pause_button);
        SubActionButton remove = builder.setContentView(removeIcon).build();

        ImageView showIcon = new ImageView(this);
        showIcon.setImageResource(R.drawable.view_button);
        SubActionButton view = builder.setContentView(showIcon).build();



        final FloatingActionMenu fam = new FloatingActionMenu.Builder(this)
                .addSubActionView(remove)
                .addSubActionView(play)
                .addSubActionView(add)
                .addSubActionView(view)
                .attachTo(fab)
                .build();

        //
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewTask.class);
                intent.putExtra("full_date", fullDate);
                intent.putExtra("day", day);
                intent.putExtra("month", monthsWithPostfix[month]);
                startActivity(intent);
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Сейчас эта функция заблокирована, она будет добавлена в следующем обновлении!", Toast.LENGTH_SHORT).show();
                fam.close(true);
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Сейчас эта функция заблокирована, она будет добавлена в следующем обновлении!", Toast.LENGTH_SHORT).show();
                fam.close(true);
            }
        });


        //Обработчик нажатия на кнопку просмотра задач
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout sv = (LinearLayout)findViewById(R.id.Scroll_layout);
                RecyclerView rv = (RecyclerView)findViewById(R.id.recyclerView);
                DBHelper dbHelper = new DBHelper(  MainActivity.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String [] args = new String[]{ fullDate };
                List<TaskCard> cards = new ArrayList<>();
                Cursor cursor = db.query("tasks", null, "task_deadline = ?", args
                        ,null, null, "task_id");
                if(cursor != null) {
                    if (cursor.moveToFirst()) {
                        String str;
                        do {
                            cards.add(new TaskCard(
                                    cursor.getString(cursor.getColumnIndex("task_deadline")),
                                    cursor.getString(cursor.getColumnIndex("task_run_time")).toString(),
                                    cursor.getString(cursor.getColumnIndex("task_comment"))
                            ));
                        } while (cursor.moveToNext());
                    }
                    TaskCardAdapter taskCardAdapter = new TaskCardAdapter(cards);
                    rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    //Устанавливаем фиксированный размер
                    rv.setHasFixedSize(true);
                    //Запрещаем прокрутку карточек(т.к. они уже размещаются в scrollview и нам не нужна двойная прокрутка)
                    rv.setNestedScrollingEnabled(false);
                    //Задаем ширину под все карточки
                    rv.setMinimumHeight(320 * taskCardAdapter.getItemCount());
                    rv.setAdapter(taskCardAdapter);
                }
                selectedDate.setText(day + " " + monthsWithPostfix[month]);
                fam.close(true);
            }
        });


        DateFormat dateFormat = new SimpleDateFormat("d-MM-YYYY");
        Date date = new Date();
        headerSelectedMonthName.setText(months[date.getMonth()] + " " + (date.getYear() + 1900));
        year = (date.getYear() + 1900) + "";
        month = date.getMonth();
        DateFormat dayFormat = new SimpleDateFormat("d");
        day = dayFormat.format(date.getTime());
        fullDate = day + "-" + (month+1) + "-" + year;


        final CompactCalendarView compactCalendarView = (CompactCalendarView) findViewById(R.id.calendarView);
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                DateFormat dateFormat = new SimpleDateFormat("d-MM-YYYY");
                fullDate = dateFormat.format(dateClicked.getTime());
                DateFormat dayFormat = new SimpleDateFormat("d");
                day = dayFormat.format(dateClicked.getTime());
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                headerSelectedMonthName.setText(months[firstDayOfNewMonth.getMonth()] + " "+(firstDayOfNewMonth.getYear() + 1900));
                month = firstDayOfNewMonth.getMonth();
                DateFormat dateFormat = new SimpleDateFormat("d-MM-YYYY");
                fullDate = dateFormat.format(firstDayOfNewMonth.getTime());
                DateFormat dayFormat = new SimpleDateFormat("d");
                day = dayFormat.format(firstDayOfNewMonth.getTime());
                year = new SimpleDateFormat("yyyy").format(firstDayOfNewMonth.getTime());
                //В поле выбранной даты устанавливаем выбранную дату
                selectedDate.setText(day + " " + monthsWithPostfix[month]);
                AddTaskOnCalendar(compactCalendarView);
            }
        });
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);

        //В поле выбранной даты устанавливаем выбранную дату
        selectedDate.setText(day + " " + monthsWithPostfix[month]);

        //Проверка, произошел ли возврат на эту страницу со страницы добавления задачи
        boolean isTaskCreated = getIntent().getBooleanExtra("new_task_created", false);
        if(isTaskCreated){
            Toast.makeText(this, "Задача успешно добавлена", Toast.LENGTH_SHORT).show();
            month = getIntent().getIntExtra("month", month);
        }
        AddTaskOnCalendar(compactCalendarView);
    }

    //Метод добавления точек(указатель задачи) на календарь
    private void AddTaskOnCalendar(CompactCalendarView compactCalendarView){
        //Добавляем отметку дедлайна на календарь
        DBHelper dbHelper = new DBHelper(  MainActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String [] args = new String[]{ fullDate };
        List<Event> cards = new ArrayList<>();
        Cursor cursor = db.query("tasks", null, "task_deadline LIKE '%-" + (month / 10)+ "" + (month % 10 +1) + "-%'", null
                ,null, null, "task_id");
        if(cursor != null) {
            if (cursor.moveToFirst()) {
                do {

                    String _deadline = cursor.getString(cursor.getColumnIndex("task_deadline"));
                    String _comment = cursor.getString(cursor.getColumnIndex("task_comment"));
                    DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    Date deadlineDate = null;
                    try {
                        deadlineDate = format.parse(_deadline);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    cards.add(new Event(Color.GREEN, deadlineDate.getTime(), _comment));

                } while (cursor.moveToNext());
            }
            compactCalendarView.addEvents(cards);
        }
    }
}
