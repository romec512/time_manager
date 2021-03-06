package com.example.roman.myapplication;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
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

import DataBase.Task;
import DataBase.TaskDistribution;
import Source.AnimationHelper;
import Source.CardVIewHelper;
import Source.DBHelper;
import Source.TaskCard;
import Source.TaskCardAdapter;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity {
    public String fullDate;
    private String day;
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

        DBHelper dbHelper = new DBHelper(this);
        dbHelper.onCreate(dbHelper.getWritableDatabase());

        startNotifications();


        ImageView icon = new ImageView(this);
        icon.setImageResource(R.drawable.logo2);
        final FloatingActionButton fab = new FloatingActionButton.Builder(this).setContentView(icon).build();
        SubActionButton.Builder builder = new SubActionButton.Builder(this);

        ImageView addIcon = new ImageView(this);
        addIcon.setImageResource(R.drawable.add_button);
        SubActionButton add = builder.setContentView(addIcon).build();

        ImageView playIcon = new ImageView(this);
        playIcon.setImageResource(R.drawable.sett_button);
        SubActionButton play = builder.setContentView(playIcon).build();

        ImageView showIcon = new ImageView(this);
        showIcon.setImageResource(R.drawable.view_button);
        SubActionButton view = builder.setContentView(showIcon).build();
        final FloatingActionMenu fam = new FloatingActionMenu.Builder(this)
                .addSubActionView(play)
                .addSubActionView(add)
//                .addSubActionView(view)
                .attachTo(fab)
                .setStartAngle(202) // 180 градусов - это 9 часов на цифербате
                .setEndAngle(248) // 270 градусов - 12 часов на циферблате(верх)
                .build();
        fam.updateItemPositions();
        //обработчик кнопки добавить
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
               Intent intent = new Intent(MainActivity.this, MyGraphics.class);
               startActivity(intent);
                fam.close(true);
            }
        });

        //Обработчик нажатия на кнопку просмотра задач
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardVIewHelper cardVIewHelper = new CardVIewHelper();
                cardVIewHelper.drawCards(fullDate, MainActivity.this, findViewById(R.id.constraintLayout));
                selectedDate.setText(day + " " + monthsWithPostfix[month]);
                fam.close(true);
            }
        });

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY");
        Date date = new Date();
        headerSelectedMonthName.setText(months[date.getMonth()] + " " + (date.getYear() + 1900));
        year = (date.getYear() + 1900) + "";
        month = date.getMonth();
        DateFormat dayFormat = new SimpleDateFormat("dd");
        day = dayFormat.format(date.getTime());
        fullDate = day + "-" + (month+1)/10 + (month+1)%10 + "-" + year;
        CardVIewHelper cardVIewHelper = new CardVIewHelper();
        cardVIewHelper.drawCards(fullDate, MainActivity.this, findViewById(R.id.constraintLayout));


        final CompactCalendarView compactCalendarView = (CompactCalendarView) findViewById(R.id.calendarView);
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                DateFormat dateFormat = new SimpleDateFormat("MM");
                String month = dateFormat.format(dateClicked);
                DateFormat dayFormat = new SimpleDateFormat("dd");
                day = dayFormat.format(dateClicked.getTime());
                fullDate = day + "-" + month + "-" + (1900 + dateClicked.getYear());
                CardVIewHelper cardVIewHelper = new CardVIewHelper();
                cardVIewHelper.drawCards(fullDate, MainActivity.this, findViewById(R.id.constraintLayout));
                AnimationHelper animationHelper = new AnimationHelper(cardVIewHelper.rv);
                animationHelper.slideIn(380 * cardVIewHelper.itemCount);
                selectedDate.setText(day + " " + monthsWithPostfix[MainActivity.this.month]);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                headerSelectedMonthName.setText(months[firstDayOfNewMonth.getMonth()] + " "+(firstDayOfNewMonth.getYear() + 1900));
                month = firstDayOfNewMonth.getMonth();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY");
                fullDate = dateFormat.format(firstDayOfNewMonth.getTime());
                DateFormat dayFormat = new SimpleDateFormat("dd");
                day = dayFormat.format(firstDayOfNewMonth.getTime());
                year = new SimpleDateFormat("yyyy").format(firstDayOfNewMonth.getTime());
                compactCalendarView.removeAllEvents();
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
    public void AddTaskOnCalendar(CompactCalendarView compactCalendarView){
        //Добавляем отметку дедлайна на календарь
        DBHelper dbHelper = new DBHelper(  MainActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String [] args = new String[]{ fullDate };
        List<Event> cards = new ArrayList<>();
        Cursor cursor = db.query("tasks", null, "task_deadline LIKE '%-" + (month / 10)+ "" + (month % 10 +1) + "-%'", null
                ,null, null, "task_priority DESC");
        if(cursor != null) {
            if (cursor.moveToFirst()) {
                do {

                    String _deadline = cursor.getString(cursor.getColumnIndex("task_deadline"));
                    String _comment = cursor.getString(cursor.getColumnIndex("task_comment"));
                    int _rating = cursor.getInt(cursor.getColumnIndex("task_priority"));
                    DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    Date deadlineDate = null;
                    try {
                        deadlineDate = format.parse(_deadline);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                     int color = Color.parseColor("#f04f54");
                    cards.add(new Event(color, deadlineDate.getTime(), _comment));

                } while (cursor.moveToNext());
            }
            Cursor tasksDistr = db.query("tasks_distribution", null, "task_date LIKE '%-" + (month / 10) + "" + (month % 10 + 1) + "-%'", null,
                    null,null, null);
            if(tasksDistr != null){
                if(tasksDistr.moveToFirst()){
                    do {
                        String taskDistrDateStr = tasksDistr.getString(tasksDistr.getColumnIndex("task_date"));
                        int color = Color.parseColor("#d0f0c0");
                        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                        Date taskDistrDate = null;
                        try {
                            taskDistrDate = format.parse(taskDistrDateStr);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        cards.add(new Event(color, taskDistrDate.getTime(), ""));
                    } while (tasksDistr.moveToNext());
                }
            }
            compactCalendarView.addEvents(cards);
        }
    }

    @Override
    public void onBackPressed()
    {
        return;
    }

    private void startNotifications(){
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ListenerService.class);
        startService(intent);
    }
}
