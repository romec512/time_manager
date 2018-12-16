package Source;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roman.myapplication.R;

import java.util.List;

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

        public TaskCardViewHolder(@NonNull View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            tvStartDate = (TextView)itemView.findViewById(R.id.start_date);
            tvEndDate = (TextView)itemView.findViewById(R.id.stop_date);
            tvComment = (TextView)itemView.findViewById(R.id.comment);
            tvDeadline = (TextView)itemView.findViewById(R.id.tvDeadline);
        }
    }
}
