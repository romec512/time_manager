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

    public int getStartHours(){
        return Integer.parseInt(startTime.split(":")[0]);
    }

    public int getStartMinutes(){
        return Integer.parseInt(startTime.split(":")[1]);
    }

    public int getEndHours(){
        return Integer.parseInt(stopTime.split(":")[0]);
    }

    public int getEndMinutes(){
        return Integer.parseInt(stopTime.split(":")[1]);
    }

    public void setStartTime(int hours, int minutes){
        startTime = hours / 10 + "" + hours % 10 + ":" + minutes / 10 + minutes % 10;
    }

    public void setStopTime(int hours, int minutes){
        stopTime = hours / 10 + "" + hours % 10 + ":" + minutes / 10 + minutes % 10;
    }

}
