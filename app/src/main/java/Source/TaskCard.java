package Source;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;

public class TaskCard extends CardView {
    public String start_date, stop_date, comment, deadline;
    public int  rating, taskId;
    public TaskCard(String start_date, String stop_date, String comment, String _deadline, int rating, int _taskId, Context context){
        super(context);
        this.rating = rating;
        this.start_date = start_date;
        this.stop_date = stop_date;
        this.comment = comment;
        this.deadline = _deadline;
        this.taskId = _taskId;
    }

}
