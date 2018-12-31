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

import DataBase.TaskDistribution;

import static java.lang.Integer.parseInt;

public class CardVIewHelper {
    public void drawCards(String fullDate, Context context,View view){
        LinearLayout sv = (LinearLayout)view.findViewById(R.id.Scroll_layout);
        RecyclerView rv = (RecyclerView)view.findViewById(R.id.recyclerView);
        DBHelper dbHelper = new DBHelper(  context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String [] args = new String[]{ fullDate };
        List<TaskCard> cards = new ArrayList<>();
        List<TaskDistribution> distributions = TaskDistribution.find(TaskDistribution.class, "task_date = ?", args, null, "start_time",null );
        for (TaskDistribution distribution: distributions) {
            cards.add(new TaskCard(distribution.startTime, distribution.stopTime, distribution.task.comment,
                    distribution.task.deadline, distribution.task.priority, distribution.task.getId().intValue(), context));
        }
            TaskCardAdapter taskCardAdapter = new TaskCardAdapter(cards,fullDate);
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
