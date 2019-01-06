package DataBase;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Table;

@Table(name = "tasks_distribution")
public class TaskDistribution extends SugarRecord implements Parcelable {
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

    protected TaskDistribution(Parcel in) {
//        task = in.readParcelable(Task.class.getClassLoader());
        String[] data = new String[3];
        in.writeStringArray(data);
        startTime = data[0];
        stopTime = data[1];
        date = data[2];
    }

    public static final Creator<TaskDistribution> CREATOR = new Creator<TaskDistribution>() {
        @Override
        public TaskDistribution createFromParcel(Parcel in) {
            return new TaskDistribution(in);
        }

        @Override
        public TaskDistribution[] newArray(int size) {
            return new TaskDistribution[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                startTime,
                stopTime,
                date
        });
        dest.writeParcelable(task, 1);
    }
}
