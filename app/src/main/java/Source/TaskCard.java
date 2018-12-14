package Source;

import android.support.v7.app.AppCompatActivity;

public class TaskCard extends AppCompatActivity {
    public String start_date, stop_date, comment;
    public int  rating;

    public TaskCard(String start_date, String stop_date, String comment, int rating){
        this.rating = rating;
        this.start_date = start_date;
        this.stop_date = stop_date;
        this.comment = comment;
    }

}
