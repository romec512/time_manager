package DataBase;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Table;

@Table(name = "tasks")
public class Task extends SugarRecord implements Parcelable {
    @Column(name = "task_deadline")
    public String deadline;
    @Column(name = "task_comment")
    public String comment;
    @Column(name = "task_run_time")
    public int runTime;
    @Column(name = "task_priority")
    public int priority;
    public Task(){

    }

    public Task(String _deadline, String _comment, int _runTime, int _priority){
        deadline = _deadline;
        comment = _comment;
        runTime = _runTime;
        priority = _priority;
    }

    protected Task(Parcel in) {
        String[] stringData = new String[2];
        in.readStringArray(stringData);
        int[] intData = new int[2];
        in.readIntArray(intData);
        deadline = stringData[0];
        comment = stringData[1];
        runTime = intData[0];
        priority = intData[1];
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                deadline,
                comment
        });
        dest.writeIntArray(new int[]{
                runTime,
                priority
        });
    }
}
