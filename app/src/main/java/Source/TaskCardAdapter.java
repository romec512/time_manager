package Source;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Selection;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.example.roman.myapplication.MainActivity;
import com.example.roman.myapplication.R;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TaskCardAdapter extends RecyclerView.Adapter<TaskCardAdapter.TaskCardViewHolder> {
    List<TaskCard> cards;
    public String [] colors= {"#d0f0c0","#ede674","#f7d420","#f6903e","#f04f54"};
    public TaskCardAdapter(List<TaskCard> _cards){
        cards = _cards;
    }


    @NonNull
    @Override
    public TaskCardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.taskcard, viewGroup, false);
        TaskCardViewHolder taskCardViewHolder = new TaskCardViewHolder(v);
        return taskCardViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskCardViewHolder taskCardViewHolder, int i) {
        taskCardViewHolder.tvStartDate.setText(cards.get(i).start_date);
        taskCardViewHolder.tvEndDate.setText(cards.get(i).stop_date);
        taskCardViewHolder.tvComment.setText(cards.get(i).comment);
        int colorIndex = cards.get(i).rating - 1;
        taskCardViewHolder.tvStartDate.setTextColor(Color.parseColor(colors[colorIndex]));
        taskCardViewHolder.tvEndDate.setTextColor(Color.parseColor(colors[colorIndex]));
        taskCardViewHolder.tvDeadline.setText(cards.get(i).deadline);
        taskCardViewHolder.taskId = cards.get(i).taskId;
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class TaskCardViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView tvStartDate, tvEndDate, tvComment, tvDeadline;
        SwipeLayout swipeLayout;
        Button buttonDelete;
        int taskId;
        public TaskCardViewHolder(@NonNull final View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            tvStartDate = (TextView)itemView.findViewById(R.id.start_date);
            tvEndDate = (TextView)itemView.findViewById(R.id.stop_date);
            tvComment = (TextView)itemView.findViewById(R.id.comment);
            tvDeadline = (TextView)itemView.findViewById(R.id.tvDeadline);
            swipeLayout = (SwipeLayout)itemView.findViewById(R.id.swipe);
            //set show mode.
            swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
            swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onClose(SwipeLayout layout) {
                    //when the SurfaceView totally cover the BottomView.
                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                    //you are swiping.
                }

                @Override
                public void onStartOpen(SwipeLayout layout) {

                }

                @Override
                public void onOpen(SwipeLayout layout) {
                    //when the BottomView totally show.
                }

                @Override
                public void onStartClose(SwipeLayout layout) {

                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                    //when user's hand released.
                }
            });
            buttonDelete = (Button)itemView.findViewById(R.id.buttonDelete);
            buttonDelete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    SweetAlertDialog newSwaDialog = new SweetAlertDialog(itemView.getContext());
                    new SweetAlertDialog(itemView.getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Подтвердите действие")
                            .setContentText("Вы действительно хотите удалить эту задачу?")
                            .setCancelText("Да")
                            .setNeutralText("Нет")
                            .hideConfirmButton()
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    DBHelper dbHelper = new DBHelper(itemView.getContext());
                                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                                    db.delete("tasks", "task_id = " + taskId, null);
                                    db.delete("tasks_distribution", "task_id = " + taskId, null);
                                    CardVIewHelper cardVIewHelper = new CardVIewHelper();
                                    MainActivity activity = (MainActivity) itemView.getContext();
                                    cardVIewHelper.drawCards(activity.fullDate, itemView.getContext(), activity.findViewById(R.id.constraintLayout));
                                    final CompactCalendarView compactCalendarView = (CompactCalendarView) activity.findViewById(R.id.calendarView);
                                    compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
                                    compactCalendarView.removeAllEvents();
                                    activity.AddTaskOnCalendar(compactCalendarView);
                                    sweetAlertDialog.dismissWithAnimation();
                                    Toast.makeText(activity, "Задача успешно удалена", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .show();
                }
            });
        }
    }
}
