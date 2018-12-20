package Source;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.example.roman.myapplication.MainActivity;
import com.example.roman.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class CardVIewHelper {
    public void drawCards(String fullDate, Context context,View view){
        LinearLayout sv = (LinearLayout)view.findViewById(R.id.Scroll_layout);
        RecyclerView rv = (RecyclerView)view.findViewById(R.id.recyclerView);
        DBHelper dbHelper = new DBHelper(  context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String [] args = new String[]{ fullDate };
        List<TaskCard> cards = new ArrayList<>();
        Cursor cursor = db.query("tasks_distribution", null, "task_date = ?", args
                ,null, null, "start_time");
        if(cursor != null) {
            if (cursor.moveToFirst()) {
                String str;
                do {
                    int taskId = cursor.getInt(cursor.getColumnIndex("task_id"));
                    String startTime = cursor.getString(cursor.getColumnIndex("start_time"));
                    String stopTime = cursor.getString(cursor.getColumnIndex("stop_time"));
                    Cursor task = db.query("tasks", null, "task_id = " + taskId, null, null, null, null);
                    task.moveToFirst();
                    TaskCard card = new TaskCard(
                            startTime,
                            stopTime,
                            task.getString(task.getColumnIndex("task_comment")),
                            task.getString(task.getColumnIndex("task_deadline")),
                            parseInt( task.getString(task.getColumnIndex("task_priority"))),
                            task.getInt(task.getColumnIndex("task_id")),
                            context
                    );
                    cards.add(card);
                } while (cursor.moveToNext());
            }
            TaskCardAdapter taskCardAdapter = new TaskCardAdapter(cards);
            rv.setLayoutManager(new LinearLayoutManager(context));
            //Устанавливаем фиксированный размер
            rv.setHasFixedSize(true);
            //Запрещаем прокрутку карточек(т.к. они уже размещаются в scrollview и нам не нужна двойная прокрутка)
            rv.setNestedScrollingEnabled(false);
            //Задаем ширину под все карточки
            rv.setMinimumHeight(380 * taskCardAdapter.getItemCount());
            rv.setAdapter(taskCardAdapter);
        }
    }
}
