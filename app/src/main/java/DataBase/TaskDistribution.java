package DataBase;

import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Table;

@Table(name = "tasks_distribution")
public class TaskDistribution extends SugarRecord {
    @Column(name = "task_id")
    public Task task;
    @Column(name = "start_time")
    public String startTime;
    @Column(name = "stop_time")
    public String stopTime;
    @Column(name = "task_date")
    public String date;

    public TaskDistribution(){

    }

    public TaskDistribution(Task _task, String _startTime, String _stopTime, String _date){
        task = _task;
        startTime = _startTime;
        stopTime = _stopTime;
        date = _date;
    }

}
