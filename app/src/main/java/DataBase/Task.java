package DataBase;

import com.orm.SugarRecord;

public class Task extends SugarRecord<Task> {
    public String deadline, comment;
    public int runTime, priority;
    public Task(){

    }


}
